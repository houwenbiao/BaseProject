package com.qtimes.utils.java;

import com.qtimes.utils.android.PluLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gufei on 2016/7/14.
 * 城市code
 */
public class CityUtils {
    private static CityUtils mCityUtils;
    private Map<Integer, City> provinces;
    private Map<Integer, City> citys;

    private CityUtils() {
        String[] provincesStr = new String[]{"北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江",
                "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西",
                "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "香港", "澳门", "台湾"
        };
        provinces = new HashMap<>();
        for (int size = provincesStr.length, i = 0; i < size; i++) {
            City city = new City();
            int key = i + 1;
            city.setKey(key);
            city.setProvince(key);
            city.setName(provincesStr[i]);
            provinces.put(key, city);
        }

        String citysString = "\n" +
                "    340344\t : \"台湾\",\n" +
                "    330343\t : \"澳门\",\n" +
                "    320342\t : \"香港\",\n" +
                "    310341\t : \"省直辖县级行政单位\",\n" +
                "    310340\t : \"阿勒泰地区\",\n" +
                "    310339\t : \"塔城地区\",\n" +
                "    310338\t : \"伊犁哈萨克自治州\",\n" +
                "    310337\t : \"和田地区\",\n" +
                "    310336\t : \"喀什地区\",\n" +
                "    310335\t : \"克孜勒苏柯尔克孜自治州\",\n" +
                "    310334\t : \"阿克苏地区\",\n" +
                "    310333\t : \"巴音郭楞蒙古自治州\",\n" +
                "    310332\t : \"博尔塔拉蒙古自治州\",\n" +
                "    310331\t : \"昌吉回族自治州\",\n" +
                "    310330\t : \"哈密地区\",\n" +
                "    310329\t : \"吐鲁番地区\",\n" +
                "    310328\t : \"克拉玛依市\",\n" +
                "    310327\t : \"乌鲁木齐市\",\n" +
                "    300326\t : \"中卫市\",\n" +
                "    300325\t : \"固原市\",\n" +
                "    300324\t : \"吴忠市\",\n" +
                "    300323\t : \"石嘴山市\",\n" +
                "    300322\t : \"银川市\",\n" +
                "    290321\t : \"海西蒙古族藏族自治州\",\n" +
                "    290320\t : \"玉树藏族自治州\",\n" +
                "    290319\t : \"果洛藏族自治州\",\n" +
                "    290318\t : \"海南藏族自治州\",\n" +
                "    290317\t : \"黄南藏族自治州\",\n" +
                "    290316\t : \"海北藏族自治州\",\n" +
                "    290315\t : \"海东地区\",\n" +
                "    290314\t : \"西宁市\",\n" +
                "    280313\t : \"甘南藏族自治州\",\n" +
                "    280312\t : \"临夏回族自治州\",\n" +
                "    280311\t : \"陇南市\",\n" +
                "    280310\t : \"定西市\",\n" +
                "    280309\t : \"庆阳市\",\n" +
                "    280308\t : \"酒泉市\",\n" +
                "    280307\t : \"平凉市\",\n" +
                "    280306\t : \"张掖市\",\n" +
                "    280305\t : \"武威市\",\n" +
                "    280304\t : \"天水市\",\n" +
                "    280303\t : \"白银市\",\n" +
                "    280302\t : \"金昌市\",\n" +
                "    280301\t : \"嘉峪关市\",\n" +
                "    280300\t : \"兰州市\",\n" +
                "    270299\t : \"商洛市\",\n" +
                "    270298\t : \"安康市\",\n" +
                "    270297\t : \"榆林市\",\n" +
                "    270296\t : \"汉中市\",\n" +
                "    270295\t : \"延安市\",\n" +
                "    270294\t : \"渭南市\",\n" +
                "    270293\t : \"咸阳市\",\n" +
                "    270292\t : \"宝鸡市\",\n" +
                "    270291\t : \"铜川市\",\n" +
                "    270290\t : \"西安市\",\n" +
                "    260289\t : \"林芝地区\",\n" +
                "    260288\t : \"阿里地区\",\n" +
                "    260287\t : \"那曲地区\",\n" +
                "    260286\t : \"日喀则地区\",\n" +
                "    260285\t : \"山南地区\",\n" +
                "    260284\t : \"昌都地区\",\n" +
                "    260283\t : \"拉萨市\",\n" +
                "    250282\t : \"迪庆藏族自治州\",\n" +
                "    250281\t : \"怒江傈僳族自治州\",\n" +
                "    250280\t : \"德宏傣族景颇族自治州\",\n" +
                "    250279\t : \"大理白族自治州\",\n" +
                "    250278\t : \"西双版纳傣族自治州\",\n" +
                "    250277\t : \"文山壮族苗族自治州\",\n" +
                "    250276\t : \"红河哈尼族彝族自治州\",\n" +
                "    250275\t : \"楚雄彝族自治州\",\n" +
                "    250274\t : \"临沧市\",\n" +
                "    250273\t : \"普洱市\",\n" +
                "    250272\t : \"丽江市\",\n" +
                "    250271\t : \"昭通市\",\n" +
                "    250270\t : \"保山市\",\n" +
                "    250269\t : \"玉溪市\",\n" +
                "    250268\t : \"曲靖市\",\n" +
                "    250267\t : \"昆明市\",\n" +
                "    240266\t : \"黔南布依族苗族自治州\",\n" +
                "    240265\t : \"黔东南苗族侗族自治州\",\n" +
                "    240264\t : \"毕节地区\",\n" +
                "    240263\t : \"黔西南布依族苗族自治州\",\n" +
                "    240262\t : \"铜仁地区\",\n" +
                "    240261\t : \"安顺市\",\n" +
                "    240260\t : \"遵义市\",\n" +
                "    240259\t : \"六盘水市\",\n" +
                "    240258\t : \"贵阳市\",\n" +
                "    230257\t : \"凉山彝族自治州\",\n" +
                "    230256\t : \"甘孜藏族自治州\",\n" +
                "    230255\t : \"阿坝藏族羌族自治州\",\n" +
                "    230254\t : \"资阳市\",\n" +
                "    230253\t : \"巴中市\",\n" +
                "    230252\t : \"雅安市\",\n" +
                "    230251\t : \"达州市\",\n" +
                "    230250\t : \"广安市\",\n" +
                "    230249\t : \"宜宾市\",\n" +
                "    230248\t : \"眉山市\",\n" +
                "    230247\t : \"南充市\",\n" +
                "    230246\t : \"乐山市\",\n" +
                "    230245\t : \"内江市\",\n" +
                "    230244\t : \"遂宁市\",\n" +
                "    230243\t : \"广元市\",\n" +
                "    230242\t : \"绵阳市\",\n" +
                "    230241\t : \"德阳市\",\n" +
                "    230240\t : \"泸州市\",\n" +
                "    230239\t : \"攀枝花市\",\n" +
                "    230238\t : \"自贡市\",\n" +
                "    230237\t : \"成都市\",\n" +
                "    220236\t : \"重庆市\",\n" +
                "    210235\t : \"省直辖县级行政单位\",\n" +
                "    210234\t : \"三亚市\",\n" +
                "    210233\t : \"海口市\",\n" +
                "    200232\t : \"崇左市\",\n" +
                "    200231\t : \"来宾市\",\n" +
                "    200230\t : \"河池市\",\n" +
                "    200229\t : \"贺州市\",\n" +
                "    200228\t : \"百色市\",\n" +
                "    200227\t : \"玉林市\",\n" +
                "    200226\t : \"贵港市\",\n" +
                "    200225\t : \"钦州市\",\n" +
                "    200224\t : \"防城港市\",\n" +
                "    200223\t : \"北海市\",\n" +
                "    200222\t : \"梧州市\",\n" +
                "    200221\t : \"桂林市\",\n" +
                "    200220\t : \"柳州市\",\n" +
                "    200219\t : \"南宁市\",\n" +
                "    190218\t : \"云浮市\",\n" +
                "    190217\t : \"揭阳市\",\n" +
                "    190216\t : \"潮州市\",\n" +
                "    190215\t : \"中山市\",\n" +
                "    190214\t : \"东莞市\",\n" +
                "    190213\t : \"清远市\",\n" +
                "    190212\t : \"阳江市\",\n" +
                "    190211\t : \"河源市\",\n" +
                "    190210\t : \"汕尾市\",\n" +
                "    190209\t : \"梅州市\",\n" +
                "    190208\t : \"惠州市\",\n" +
                "    190207\t : \"肇庆市\",\n" +
                "    190206\t : \"茂名市\",\n" +
                "    190205\t : \"湛江市\",\n" +
                "    190204\t : \"江门市\",\n" +
                "    190203\t : \"佛山市\",\n" +
                "    190202\t : \"汕头市\",\n" +
                "    190201\t : \"珠海市\",\n" +
                "    190200\t : \"深圳市\",\n" +
                "    190199\t : \"韶关市\",\n" +
                "    190198\t : \"广州市\",\n" +
                "    180197\t : \"湘西土家族苗族自治州\",\n" +
                "    180196\t : \"娄底市\",\n" +
                "    180195\t : \"怀化市\",\n" +
                "    180194\t : \"永州市\",\n" +
                "    180193\t : \"郴州市\",\n" +
                "    180192\t : \"益阳市\",\n" +
                "    180191\t : \"张家界市\",\n" +
                "    180190\t : \"常德市\",\n" +
                "    180189\t : \"岳阳市\",\n" +
                "    180188\t : \"邵阳市\",\n" +
                "    180187\t : \"衡阳市\",\n" +
                "    180186\t : \"湘潭市\",\n" +
                "    180185\t : \"株洲市\",\n" +
                "    180184\t : \"长沙市\",\n" +
                "    170183\t : \"省直辖县级行政单位\",\n" +
                "    170182\t : \"恩施土家族苗族自治州\",\n" +
                "    170181\t : \"随州市\",\n" +
                "    170180\t : \"咸宁市\",\n" +
                "    170179\t : \"黄冈市\",\n" +
                "    170178\t : \"荆州市\",\n" +
                "    170177\t : \"孝感市\",\n" +
                "    170176\t : \"荆门市\",\n" +
                "    170175\t : \"鄂州市\",\n" +
                "    170174\t : \"襄樊市\",\n" +
                "    170173\t : \"宜昌市\",\n" +
                "    170172\t : \"十堰市\",\n" +
                "    170171\t : \"黄石市\",\n" +
                "    170170\t : \"武汉市\",\n" +
                "    160169\t : \"济源市\",\n" +
                "    160168\t : \"驻马店市\",\n" +
                "    160167\t : \"周口市\",\n" +
                "    160166\t : \"信阳市\",\n" +
                "    160165\t : \"商丘市\",\n" +
                "    160164\t : \"南阳市\",\n" +
                "    160163\t : \"三门峡市\",\n" +
                "    160162\t : \"漯河市\",\n" +
                "    160161\t : \"许昌市\",\n" +
                "    160160\t : \"濮阳市\",\n" +
                "    160159\t : \"焦作市\",\n" +
                "    160158\t : \"新乡市\",\n" +
                "    160157\t : \"鹤壁市\",\n" +
                "    160156\t : \"安阳市\",\n" +
                "    160155\t : \"平顶山市\",\n" +
                "    160154\t : \"洛阳市\",\n" +
                "    160153\t : \"开封市\",\n" +
                "    160152\t : \"郑州市\",\n" +
                "    150151\t : \"菏泽市\",\n" +
                "    150150\t : \"滨州市\",\n" +
                "    150149\t : \"聊城市\",\n" +
                "    150148\t : \"德州市\",\n" +
                "    150147\t : \"临沂市\",\n" +
                "    150146\t : \"莱芜市\",\n" +
                "    150145\t : \"日照市\",\n" +
                "    150144\t : \"威海市\",\n" +
                "    150143\t : \"泰安市\",\n" +
                "    150142\t : \"济宁市\",\n" +
                "    150141\t : \"潍坊市\",\n" +
                "    150140\t : \"烟台市\",\n" +
                "    150139\t : \"东营市\",\n" +
                "    150138\t : \"枣庄市\",\n" +
                "    150137\t : \"淄博市\",\n" +
                "    150136\t : \"青岛市\",\n" +
                "    150135\t : \"济南市\",\n" +
                "    140134\t : \"上饶市\",\n" +
                "    140133\t : \"抚州市\",\n" +
                "    140132\t : \"宜春市\",\n" +
                "    140131\t : \"吉安市\",\n" +
                "    140130\t : \"赣州市\",\n" +
                "    140129\t : \"鹰潭市\",\n" +
                "    140128\t : \"新余市\",\n" +
                "    140127\t : \"九江市\",\n" +
                "    140126\t : \"萍乡市\",\n" +
                "    140125\t : \"景德镇市\",\n" +
                "    140124\t : \"南昌市\",\n" +
                "    130123\t : \"宁德市\",\n" +
                "    130122\t : \"龙岩市\",\n" +
                "    130121\t : \"南平市\",\n" +
                "    130120\t : \"漳州市\",\n" +
                "    130119\t : \"泉州市\",\n" +
                "    130118\t : \"三明市\",\n" +
                "    130117\t : \"莆田市\",\n" +
                "    130116\t : \"厦门市\",\n" +
                "    130115\t : \"福州市\",\n" +
                "    120114\t : \"宣城市\",\n" +
                "    120113\t : \"池州市\",\n" +
                "    120112\t : \"亳州市\",\n" +
                "    120111\t : \"六安市\",\n" +
                "    120110\t : \"巢湖市\",\n" +
                "    120109\t : \"宿州市\",\n" +
                "    120108\t : \"阜阳市\",\n" +
                "    120107\t : \"滁州市\",\n" +
                "    120106\t : \"黄山市\",\n" +
                "    120105\t : \"安庆市\",\n" +
                "    120104\t : \"铜陵市\",\n" +
                "    120103\t : \"淮北市\",\n" +
                "    120102\t : \"马鞍山市\",\n" +
                "    120101\t : \"淮南市\",\n" +
                "    120100\t : \"蚌埠市\",\n" +
                "    120099\t : \"芜湖市\",\n" +
                "    120098\t : \"合肥市\",\n" +
                "    110097\t : \"丽水市\",\n" +
                "    110096\t : \"台州市\",\n" +
                "    110095\t : \"舟山市\",\n" +
                "    110094\t : \"衢州市\",\n" +
                "    110093\t : \"金华市\",\n" +
                "    110092\t : \"绍兴市\",\n" +
                "    110091\t : \"湖州市\",\n" +
                "    110090\t : \"嘉兴市\",\n" +
                "    110089\t : \"温州市\",\n" +
                "    110088\t : \"宁波市\",\n" +
                "    110087\t : \"杭州市\",\n" +
                "    100086\t : \"宿迁市\",\n" +
                "    100085\t : \"泰州市\",\n" +
                "    100084\t : \"镇江市\",\n" +
                "    100083\t : \"扬州市\",\n" +
                "    100082\t : \"盐城市\",\n" +
                "    100081\t : \"淮安市\",\n" +
                "    100080\t : \"连云港市\",\n" +
                "    100079\t : \"南通市\",\n" +
                "    100078\t : \"苏州市\",\n" +
                "    100077\t : \"常州市\",\n" +
                "    100076\t : \"徐州市\",\n" +
                "    100075\t : \"无锡市\",\n" +
                "    100074\t : \"南京市\",\n" +
                "    90073\t : \"上海市\",\n" +
                "    80072\t : \"大兴安岭地区\",\n" +
                "    80071\t : \"绥化市\",\n" +
                "    80070\t : \"黑河市\",\n" +
                "    80069\t : \"牡丹江市\",\n" +
                "    80068\t : \"七台河市\",\n" +
                "    80067\t : \"佳木斯市\",\n" +
                "    80066\t : \"伊春市\",\n" +
                "    80065\t : \"大庆市\",\n" +
                "    80064\t : \"双鸭山市\",\n" +
                "    80063\t : \"鹤岗市\",\n" +
                "    80062\t : \"鸡西市\",\n" +
                "    80061\t : \"齐齐哈尔市\",\n" +
                "    80060\t : \"哈尔滨市\",\n" +
                "    70059\t : \"延边朝鲜族自治州\",\n" +
                "    70058\t : \"白城市\",\n" +
                "    70057\t : \"松原市\",\n" +
                "    70056\t : \"白山市\",\n" +
                "    70055\t : \"通化市\",\n" +
                "    70054\t : \"辽源市\",\n" +
                "    70053\t : \"四平市\",\n" +
                "    70052\t : \"吉林市\",\n" +
                "    70051\t : \"长春市\",\n" +
                "    60050\t : \"葫芦岛市\",\n" +
                "    60049\t : \"朝阳市\",\n" +
                "    60048\t : \"铁岭市\",\n" +
                "    60047\t : \"盘锦市\",\n" +
                "    60046\t : \"辽阳市\",\n" +
                "    60045\t : \"阜新市\",\n" +
                "    60044\t : \"营口市\",\n" +
                "    60043\t : \"锦州市\",\n" +
                "    60042\t : \"丹东市\",\n" +
                "    60041\t : \"本溪市\",\n" +
                "    60040\t : \"抚顺市\",\n" +
                "    60039\t : \"鞍山市\",\n" +
                "    60038\t : \"大连市\",\n" +
                "    60037\t : \"沈阳市\",\n" +
                "    50036\t : \"阿拉善盟\",\n" +
                "    50035\t : \"锡林郭勒盟\",\n" +
                "    50034\t : \"兴安盟\",\n" +
                "    50033\t : \"乌兰察布市\",\n" +
                "    50032\t : \"巴彦淖尔市\",\n" +
                "    50031\t : \"呼伦贝尔市\",\n" +
                "    50030\t : \"鄂尔多斯市\",\n" +
                "    50029\t : \"通辽市\",\n" +
                "    50028\t : \"赤峰市\",\n" +
                "    50027\t : \"乌海市\",\n" +
                "    50026\t : \"包头市\",\n" +
                "    50025\t : \"呼和浩特市\",\n" +
                "    40024\t : \"吕梁市\",\n" +
                "    40023\t : \"临汾市\",\n" +
                "    40022\t : \"忻州市\",\n" +
                "    40021\t : \"运城市\",\n" +
                "    40020\t : \"晋中市\",\n" +
                "    40019\t : \"朔州市\",\n" +
                "    40018\t : \"晋城市\",\n" +
                "    40017\t : \"长治市\",\n" +
                "    40016\t : \"阳泉市\",\n" +
                "    40015\t : \"大同市\",\n" +
                "    40014\t : \"太原市\",\n" +
                "    30013\t : \"衡水市\",\n" +
                "    30012\t : \"廊坊市\",\n" +
                "    30011\t : \"沧州市\",\n" +
                "    30010\t : \"承德市\",\n" +
                "    30009\t : \"张家口市\",\n" +
                "    30008\t : \"保定市\",\n" +
                "    30007\t : \"邢台市\",\n" +
                "    30006\t : \"邯郸市,\n" +
                "\t30005\t : \"秦皇岛市\",\n" +
                "    30004\t : \"唐山市\",\n" +
                "    30003\t : \"石家庄市\",\n" +
                "    20002\t : \"天津市\",\n" +
                "    10001\t : \"北京市\"";

        String[] str = citysString.split(",");
        citys = new HashMap<>();
        for (String aStr : str) {
            String strCity = aStr.trim();
            Object[] obj     = strCity.split(":");
            if (obj.length > 1) {
                int  key  = Integer.valueOf(obj[0].toString().trim());
                City city = new City();
                city.setKey(key);
                city.setProvince(key / 10000);
                String name = String.valueOf(obj[1]).trim();
                name = name.replaceAll("\"", "");
                city.setName(name);
                citys.put(key, city);
            }
        }
    }

    public static CityUtils getInstance() {
        synchronized (CityUtils.class) {
            if (mCityUtils == null) {
                mCityUtils = new CityUtils();
            }
            return mCityUtils;
        }
    }

    public List<City> queryAllProvince() {
        List<City> list = new ArrayList<>();
        for (int i = 0; i < provinces.size(); i++) {
            City city = provinces.get(i + 1);
            if (city == null) continue;
            list.add(city);
            PluLog.d("provinces:" + city.getKey() + "|" + city.getName());
        }
        return list;
    }

    public List<City> queryCityList(int province) {
        List<City> queryCitys = new ArrayList<>();
        if (province > 10000) {//转换成一级
            province = province / 10000;
        }
        for (Integer key : citys.keySet()) {
            City city = citys.get(key);
            if (city == null) continue;
            if (province == (city.getKey() / 10000)) {
                queryCitys.add(city);
            }
        }
        Collections.sort(queryCitys, new Comparator<City>() {
            @Override
            public int compare(City lhs, City rhs) {
                if (lhs.getKey() < rhs.getKey()) {
                    return -1;
                } else return 1;
            }
        });
        return queryCitys;
    }

    public City queryCity(int cityCode) {
        City city = null;
        if (cityCode > 10000) {
            city = citys.get(cityCode);
        } else {
            city = provinces.get(cityCode);
        }
        return city;
    }

    public static String getCityImg(int cityId) {
        int cityCode = cityId / 10000;
        String cityName = getCityName(cityCode, cityId);
        String imgUrl = "";
        if (!"".equals(cityName)) {
            imgUrl = String.format("http://r.plures.net/b/dialect/dialect_gift/%s.png", cityName);
        }
        return imgUrl;
    }

    private static String getCityName(int code, int cityId) {
        String cityName = "";
        switch (code) {
            case 1:
                return "beijing";
            case 2:
                return "tianjing";
            case 3:
                return "hebei";
            case 4:
                return "shanxi";
            case 5:
                return "neimenggu";
            case 6:
                return "liaoning";
            case 7:
                return "jilin";
            case 8:
                return "heilongjiang";
            case 9:
                return "shanghai";
            case 10:
                cityName = getCityName(cityId);
                if ("".equals(cityName)) {
                    cityName = "jiangsu";
                }
                return cityName;
            case 11:
                cityName = getCityName(cityId);
                if ("".equals(cityName)) {
                    cityName = "zhejiang";
                }
                return cityName;
            case 12:
                return "anhui";
            case 13:
                cityName = getCityName(cityId);
                if ("".equals(cityName)) {
                    cityName = "fujian";
                }
                return cityName;
            case 14:
                return "jiangxi";
            case 15:
                return "shandong";
            case 16:
                return "henan";
            case 17:
                return "hubei";
            case 18:
                return "hunan";
            case 19:
                cityName = getCityName(cityId);
                if ("".equals(cityName)) {
                    cityName = "guangdong";
                }
                return cityName;
            case 20:
                return "guangxu";
            case 21:
                return "hainan";
            case 22:
                return "chongqin";
            case 23:
                return "sichuan";
            case 24:
                return "guizhou";
            case 25:
                return "yunnan";
            case 26:
                return "xizang";
            case 27:
                return "shan3xi";
            case 28:
                return "gansu";
            case 29:
                return "qinghai";
            case 30:
                return "ningxia";
            case 31:
                return "xinjiang";
            case 32:
                return "xianggang";
            case 33:
                return "aomen";
            case 34:
                return "taiwan";
            default:
                return "";
        }
    }

    private static String getCityName(int cityId) {
        switch (cityId) {
//            "name":"jiangsu",
            case 100074:
                return "diao";
            case 100075:
                return "jiegun";
            case 100076:
                return "bansi";
            case 100077:
                return "jiegun";
            case 100078:
                return "jiegun";
            case 100079:
                return "laisi";
            case 100080:
                return "laishi";
            case 100081:
                return "laisi";
            case 100082:
                return "laisi";
            case 100083:
                return "laisi";
            case 100084:
                return "ddyb";
            case 100085:
                return "laisi";
            case 100086:
                return "laishi";
//            "name":"zhejiang",
            case 110087:
                return "jg";
            case 110088:
                return "jg";
            case 110089:
                return "jg";
            case 110090:
                return "jg";
            case 110091:
                return "jg";
            case 110092:
                return "jg";
            case 110093:
                return "jg";
            case 110094:
                return "jg";
            case 110095:
                return "sege";
            case 110096:
                return "sjdm";
            case 110097:
                return "jg";
//            "name":"fujian",
            case 130115:
                return "yaba";
            case 130116:
                return "yalihai";
            case 130117:
                return "yaba";
            case 130118:
                return "yaba";
            case 130119:
                return "mubi";
            case 130120:
                return "mubi";
            case 130121:
                return "yaba";
            case 130122:
                return "yalihai";
            case 130123:
                return "yaba";
//            "name":"guangdong",
            case 190198:
                return "housailei";
            case 190200:
                return "housailei";
            case 190201:
                return "langxian";
            case 190214:
                return "housailei";
            case 190203:
                return "housailei";
            case 190215:
                return "yingcheng";
            case 190208:
                return "yingcheng";
            case 190202:
                return "langxian";
            case 190204:
                return "yingcheng";
            case 190206:
                return "xili";
            case 190207:
                return "yingcheng";
            case 190205:
                return "xili";
            case 190209:
                return "yingcheng";
            case 190210:
                return "yingcheng";
            case 190211:
                return "yingcheng";
            case 190213:
                return "yingcheng";
            case 190199:
                return "yingcheng";
            case 190217:
                return "langxian";
            case 190212:
                return "housailei";
            case 190216:
                return "langxian";
            case 190218:
                return "yingcheng";
            default:
                return "";
        }
    }

    public class City {
        int province;//对应的省
        int key;
        String name;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

    }
}
