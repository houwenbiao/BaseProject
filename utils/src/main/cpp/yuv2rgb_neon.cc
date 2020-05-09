/**--------------------------------------------------------------------------
NV21 格式转 ARGB 格式。
NV21 格式中，Y 单独存储，UV分量交错存储，YYYYVU

使用如下公式：
R = Y + 1.402*(V-128);
G = Y - 0.34414*(U-128) - 0.71414*(V-128);
B = Y + 1.772*(U-128);
浮点乘法用 6位精度处理（即a*b = ((a << 6)*b )>>6
--------------------------------------------------------------------------**/

#ifdef __arm__
#include <arm_neon.h>
#endif

#include "yuv2rgb.h"
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "YUV", ##__VA_ARGS__);

void ConvertYUV420SPToARGB8888Neon(unsigned char *yuv, int w, int h, unsigned int *rgba) {
    for (int i = 0; i < h; ++i) {
        unsigned char *dst = (unsigned char *) (rgba + w * i);
        unsigned char *y = yuv + w * i;
        unsigned char *uv = yuv + w * h + w * (i >> 1);
        int count = w;

#ifdef __arm__
        /*一次处理16个像素*/
                int c = count / 16;
                asm volatile(
                        "mov r4, %[c]\t\n"
                        "beq 2f\t\n"
                        "vmov.u8 d7, #255\t\n"//Alpha
                        "vmov.u8 d3, #255\t\n"//Alpha
                        "vmov.s16 q11, #90\t\n"
                        "vmov.s16 q12, #128\t\n"
                        "vmov.s16 q13, #21\t\n"
                        "vmov.s16 q14, #46\t\n"
                        "vmov.s16 q15, #113\t\n"
                        "1:\t\n"
                        /*Y1 Y2 是交错的两组像素的Y分量，与 UV分量值 正好一一对应*/
                        "vld2.8 {d8, d9}, [%[y]]!\t\n"//Y1, Y2
                        /*交错取出 UV 值*/
                        "vld2.8 {d0, d1}, [%[uv]]!\t\n"//u, v
                        "vmovl.u8 q5, d0\t\n"
                        "vmovl.u8 q6, d1\t\n"
                        "vsub.i16 q5, q5, q12\t\n"//U
                        "vsub.i16 q6, q6, q12\t\n"//V
                        //First RGBA
                        "vshll.u8 q7, d8, #6\t\n"
                        "vshll.u8 q8, d8, #6\t\n"
                        "vshll.u8 q9, d8, #6\t\n"
                        "vmla.i16 q7, q6, q11\t\n"
                        "vmls.i16 q8, q5, q13\t\n"
                        "vmls.i16 q8, q6, q14\t\n"
                        "vmla.i16 q9, q5, q15\t\n"

                        "vshr.s16 q7, q7, #6\t\n"
                        "vshr.s16 q8, q8, #6\t\n"
                        "vshr.s16 q9, q9, #6\t\n"
                        "vmov.s16 q10, #0\t\n"
                        "vmax.s16 q7, q7, q10\t\n"
                        "vmax.s16 q8, q8, q10\t\n"
                        "vmax.s16 q9, q9, q10\t\n"
                        "vmov.u16 q10, #255\t\n"
                        "vmin.u16 q7, q7, q10\t\n"
                        "vmin.u16 q8, q8, q10\t\n"
                        "vmin.u16 q9, q9, q10\t\n"
                        "vmovn.s16 d2, q7\t\n"
                        "vmovn.s16 d1, q8\t\n"
                        "vmovn.s16 d0, q9\t\n"

                        //Second RGBA
                        "vshll.u8 q7, d9, #6\t\n"
                        "vshll.u8 q8, d9, #6\t\n"
                        "vshll.u8 q9, d9, #6\t\n"
                        "vmla.i16 q7, q6, q11\t\n"
                        "vmls.i16 q8, q5, q13\t\n"
                        "vmls.i16 q8, q6, q14\t\n"
                        "vmla.i16 q9, q5, q15\t\n"
                        "vshr.s16 q7, q7, #6\t\n"
                        "vshr.s16 q8, q8, #6\t\n"
                        "vshr.s16 q9, q9, #6\t\n"
                        "vmov.s16 q10, #0\t\n"
                        "vmax.s16 q7, q7, q10\t\n"
                        "vmax.s16 q8, q8, q10\t\n"
                        "vmax.s16 q9, q9, q10\t\n"
                        "vmov.u16 q10, #255\t\n"
                        "vmin.u16 q7, q7, q10\t\n"
                        "vmin.u16 q8, q8, q10\t\n"
                        "vmin.u16 q9, q9, q10\t\n"
                        "vmovn.s16 d6, q7\t\n"
                        "vmovn.s16 d5, q8\t\n"
                        "vmovn.s16 d4, q9\t\n"
        /*目前我们得到的两组RGB分量值是交错的，
        * 比如：
        * d0 : (g0 g2 g4 g6 g8 g10 g12 g14)
        * d4 : (g1 g3 g5 g7 g9 g11 g13 g15)
        * 需要做交织，变成如下再存储：
        * d0 ：(g0 g1 g2 g3 g4 g5 g6 g7)
        * d4 ：(g8 g9 g10 g11 g12 g13 g14 g15)
        */
                        "vtrn.8 d2, d6\t\n"
                        "vtrn.16 d2, d6\t\n"
                        "vtrn.32 d2, d6\t\n"

                        "vtrn.8 d1, d5\t\n"
                        "vtrn.16 d1, d5\t\n"
                        "vtrn.32 d1, d5\t\n"

                        "vtrn.8 d0, d4\t\n"
                        "vtrn.16 d0, d4\t\n"
                        "vtrn.32 d0, d4\t\n"

                        "vst4.8 {d0-d3}, [%[dst]]!\t\n"
                        "vst4.8 {d4-d7}, [%[dst]]!\t\n"

                        "subs r4, r4, #1\t\n"
                        "bne 1b\t\n"
                        "2:\t\n"
                        : [dst] "+r" (dst), [y] "+r" (y), [uv] "+r" (uv), [c] "+r" (c)
                        :
                        : "r4","cc","memory","d0","d1","d2","d3","d4","d5","d6","d7","d8","d9","d10","d11","d12","d13","d14","d15","d16","d17","d18","d19","d20","d21","d22","d23","d24","d25","d26","d27","d28","d29","d30","d31"
                );
                count %= 16;
#endif

/*边角料的处理*/
        int r, g, b;
        while (count > 1) {
            unsigned char _y = y[0];
            unsigned char _u = uv[0];
            unsigned char _v = uv[1];

            r = _y + ((179 * (_v - 128)) >> 7);
            g = _y - ((43 * (_u - 128) - 91 * (_v - 128)) >> 7);
            b = _y + ((227 * (_u - 128)) >> 7);

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
            y++;
            dst += 4;
            _y = y[0];

            r = _y + ((179 * (_v - 128)) >> 7);
            g = _y - ((43 * (_u - 128) - 91 * (_v - 128)) >> 7);
            b = _y + ((227 * (_u - 128)) >> 7);

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
            y++;
            uv += 2;
            dst += 4;

            count -= 2;
        }

        if (count > 0) {
            unsigned char _y = y[0];
            unsigned char _u = uv[0];
            unsigned char _v = uv[1];
            r = _y + ((179 * (_v - 128)) >> 7);
            g = _y - ((43 * (_u - 128) - 91 * (_v - 128)) >> 7);
            b = _y + ((227 * (_u - 128)) >> 7);

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
        }
    }
}

/**--------------------------------------------------------------------------
NV21 格式转 ARGB 格式。
NV21 格式中，Y 单独存储，UV分量交错存储，YYYYVU

使用如下公式：
nR = (int)(1.164 * nY + 2.018 * nU);
nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
nB = (int)(1.164 * nY + 1.596 * nV);
浮点乘法用 6位精度处理（即a*b = ((a << 6)*b )>>6
--------------------------------------------------------------------------**/
void ConvertYUV420SPToARGB8888Neon2(unsigned char *yuv, int w, int h, unsigned int *rgba) {
    const int size = w * h;

    for (int i = 0; i < h; ++i) {
        unsigned char *dst = (unsigned char *) (rgba + w * i);
        unsigned char *y = yuv + w * i;
        unsigned char *uv = yuv + size + w * (i >> 1);
        int count = w;

#ifdef __arm__
        /*一次处理16个像素*/
        int c = count / 16;
        asm volatile(
                "mov r4, %[c]\t\n"
                "beq 2f\t\n"
                "vmov.u8 d7, #255\t\n"//Alpha
                "vmov.u8 d3, #255\t\n"//Alpha
                "vmov.s16 q11, #90\t\n"
                "vmov.s16 q12, #128\t\n"
                "vmov.s16 q13, #21\t\n"
                "vmov.s16 q14, #46\t\n"
                "vmov.s16 q15, #113\t\n"
                "1:\t\n"
                /*Y1 Y2 是交错的两组像素的Y分量，与 UV分量值 正好一一对应*/
                "vld2.8 {d8, d9}, [%[y]]!\t\n"//Y1, Y2
                /*交错取出 UV 值*/
                "vld2.8 {d0, d1}, [%[uv]]!\t\n"//u, v
                "vmovl.u8 q5, d0\t\n"
                "vmovl.u8 q6, d1\t\n"
                "vsub.i16 q5, q5, q12\t\n"//U
                "vsub.i16 q6, q6, q12\t\n"//V
                //First RGBA
                "vshll.u8 q7, d8, #6\t\n"
                "vshll.u8 q8, d8, #6\t\n"
                "vshll.u8 q9, d8, #6\t\n"
                "vmla.i16 q7, q6, q11\t\n"
                "vmls.i16 q8, q5, q13\t\n"
                "vmls.i16 q8, q6, q14\t\n"
                "vmla.i16 q9, q5, q15\t\n"

                "vshr.s16 q7, q7, #6\t\n"
                "vshr.s16 q8, q8, #6\t\n"
                "vshr.s16 q9, q9, #6\t\n"
                "vmov.s16 q10, #0\t\n"
                "vmax.s16 q7, q7, q10\t\n"
                "vmax.s16 q8, q8, q10\t\n"
                "vmax.s16 q9, q9, q10\t\n"
                "vmov.u16 q10, #255\t\n"
                "vmin.u16 q7, q7, q10\t\n"
                "vmin.u16 q8, q8, q10\t\n"
                "vmin.u16 q9, q9, q10\t\n"
                "vmovn.s16 d2, q7\t\n"
                "vmovn.s16 d1, q8\t\n"
                "vmovn.s16 d0, q9\t\n"

                //Second RGBA
                "vshll.u8 q7, d9, #6\t\n"
                "vshll.u8 q8, d9, #6\t\n"
                "vshll.u8 q9, d9, #6\t\n"
                "vmla.i16 q7, q6, q11\t\n"
                "vmls.i16 q8, q5, q13\t\n"
                "vmls.i16 q8, q6, q14\t\n"
                "vmla.i16 q9, q5, q15\t\n"
                "vshr.s16 q7, q7, #6\t\n"
                "vshr.s16 q8, q8, #6\t\n"
                "vshr.s16 q9, q9, #6\t\n"
                "vmov.s16 q10, #0\t\n"
                "vmax.s16 q7, q7, q10\t\n"
                "vmax.s16 q8, q8, q10\t\n"
                "vmax.s16 q9, q9, q10\t\n"
                "vmov.u16 q10, #255\t\n"
                "vmin.u16 q7, q7, q10\t\n"
                "vmin.u16 q8, q8, q10\t\n"
                "vmin.u16 q9, q9, q10\t\n"
                "vmovn.s16 d6, q7\t\n"
                "vmovn.s16 d5, q8\t\n"
                "vmovn.s16 d4, q9\t\n"
/*目前我们得到的两组RGB分量值是交错的，
* 比如：
* d0 : (g0 g2 g4 g6 g8 g10 g12 g14)
* d4 : (g1 g3 g5 g7 g9 g11 g13 g15)
* 需要做交织，变成如下再存储：
* d0 ：(g0 g1 g2 g3 g4 g5 g6 g7)
* d4 ：(g8 g9 g10 g11 g12 g13 g14 g15)
*/
                "vtrn.8 d2, d6\t\n"
                "vtrn.16 d2, d6\t\n"
                "vtrn.32 d2, d6\t\n"

                "vtrn.8 d1, d5\t\n"
                "vtrn.16 d1, d5\t\n"
                "vtrn.32 d1, d5\t\n"

                "vtrn.8 d0, d4\t\n"
                "vtrn.16 d0, d4\t\n"
                "vtrn.32 d0, d4\t\n"

                "vst4.8 {d0-d3}, [%[dst]]!\t\n"
                "vst4.8 {d4-d7}, [%[dst]]!\t\n"

                "subs r4, r4, #1\t\n"
                "bne 1b\t\n"
                "2:\t\n"
                : [dst] "+r" (dst), [y] "+r" (y), [uv] "+r" (uv), [c] "+r" (c)
                :
                : "r4","cc","memory","d0","d1","d2","d3","d4","d5","d6","d7","d8","d9","d10","d11","d12","d13","d14","d15","d16","d17","d18","d19","d20","d21","d22","d23","d24","d25","d26","d27","d28","d29","d30","d31"
        );
        count %= 16;
#endif

/*边角料的处理*/
        int r, g, b, t;
        while (count > 1) {
            int _y = y[0] < 16 ? 0 : (y[0] - 16);
            int _v = uv[0] - 128;
            int _u = uv[1] - 128;

            t = 149 * _y;
            r = (t + 258 * _u) >> 7;
            g = (t - 104 * _v - 50 * _u) >> 7;
            b = (t + 204 * _v) >> 7;

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
            y++;
            dst += 4;
            _y = y[0] < 16 ? 0 : (y[0] - 16);

            t = 149 * _y;
            r = (t + 258 * _u) >> 7;
            g = (t - 104 * _v - 50 * _u) >> 7;
            b = (t + 204 * _v) >> 7;

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
            y++;
            uv += 2;
            dst += 4;

            count -= 2;
        }

        if (count > 0) {
            int _y = y[0] < 16 ? 0 : (y[0] - 16);
            int _v = uv[0] - 128;
            int _u = uv[1] - 128;

            t = 149 * _y;
            r = (t + 258 * _u) >> 7;
            g = (t - 104 * _v - 50 * _u) >> 7;
            b = (t + 204 * _v) >> 7;

            if (r < 0) r = 0;
            if (r > 255) r = 255;
            if (g < 0) g = 0;
            if (g > 255) g = 255;
            if (b < 0) b = 0;
            if (b > 255) b = 255;

            dst[0] = r;
            dst[1] = g;
            dst[2] = b;
            dst[3] = 0xFF;
        }
    }
}

