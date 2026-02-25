import 'dart:convert';
import 'package:http/http.dart' as http;
import 'token_store.dart';

class ApiClient {
  final String baseUrl;
  final TokenStore tokenStore;

  ApiClient({required this.baseUrl, required this.tokenStore});

  Future<Map<String, String>> _headers({bool jsonBody = true}) async {
    final token = await tokenStore.getToken();
    final headers = <String, String>{};
    if (jsonBody) headers['Content-Type'] = 'application/json';
    if (token != null) headers['Authorization'] = 'Bearer $token';
    return headers;
  }

  Future<dynamic> get(String path) async {
    final res = await http.get(Uri.parse('$baseUrl$path'), headers: await _headers());
    return _handle(res);
  }

  Future<dynamic> post(String path, {Object? body}) async {
    final res = await http.post(
      Uri.parse('$baseUrl$path'),
      headers: await _headers(),
      body: body == null ? null : jsonEncode(body),
    );
    return _handle(res);
  }

  Future<dynamic> delete(String path) async {
    final res = await http.delete(Uri.parse('$baseUrl$path'), headers: await _headers());
    return _handle(res);
  }

  dynamic _handle(http.Response res) {
    final isJson = res.headers['content-type']?.contains('application/json') ?? false;
    final payload = isJson && res.body.isNotEmpty ? jsonDecode(res.body) : res.body;

    if (res.statusCode >= 200 && res.statusCode < 300) return payload;

    final message = isJson && payload is Map && payload['message'] != null
        ? payload['message'].toString()
        : 'Request failed (${res.statusCode})';

    throw ApiException(res.statusCode, message);
  }
}

class ApiException implements Exception {
  final int status;
  final String message;
  ApiException(this.status, this.message);

  @override
  String toString() => 'ApiException($status): $message';
}
