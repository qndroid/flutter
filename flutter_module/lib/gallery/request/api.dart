import 'package:http_plugin/http_plugin.dart';
import '../model/home_model.dart';
import 'dart:convert';

Future<HomeData> getHomeData() async {
  //模拟请求参数
  var demoData = {
    "url": "http://api.ca.beike.com/get_home_data",
  };
  final String result = await HttpPlugin.post(demoData);
  return HomeData.fromJson(json.decode(result));
}
