class AppConfig {
  static const String pcIp = '192.168.100.67'; // your IP
  //static const String pcIp = '127.0.0.1'; // your IP
  static String get baseUrl {
    return 'http://$pcIp:8080';
  }
}