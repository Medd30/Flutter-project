import 'api_client.dart';

class AuthApi {
  final ApiClient api;
  AuthApi(this.api);

  Future<Map<String, dynamic>> registerEmail({
    required String username,
    required String email,
    required String password,
  }) async {
    return (await api.post('/api/auth/register/email', body: {
      'username': username,
      'email': email,
      'password': password,
    })) as Map<String, dynamic>;
  }

  Future<Map<String, dynamic>> registerPhone({
    required String username,
    required String phone,
    required String password,
  }) async {
    return (await api.post('/api/auth/register/phone', body: {
      'username': username,
      'phone': phone,
      'password': password,
    })) as Map<String, dynamic>;
  }

  Future<void> verifyEmail({required int userId, required String code}) async {
    await api.post('/api/auth/verify/email', body: {
      'userId': userId,
      'code': code,
    });
  }

  Future<void> verifyPhone({required int userId, required String code}) async {
    await api.post('/api/auth/verify/phone', body: {
      'userId': userId,
      'code': code,
    });
  }

  Future<Map<String, dynamic>> login({
    required String identifier,
    required String password,
  }) async {
    return (await api.post('/api/auth/login', body: {
      'identifier': identifier,
      'password': password,
    })) as Map<String, dynamic>;
  }
}