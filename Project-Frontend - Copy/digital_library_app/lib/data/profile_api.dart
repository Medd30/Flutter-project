import 'api_client.dart';

class ProfileApi {
  final ApiClient api;
  ProfileApi(this.api);

  Future<Map<String, dynamic>> me() async {
    final res = await api.get('/api/me/profile');
    return res as Map<String, dynamic>;
  }

  Future<Map<String, dynamic>> updateUsername(String username) async {
    final res = await api.post(
      '/api/me/profile',
      body: {'username': username},
    );
    return res as Map<String, dynamic>;
  }

  Future<void> changePassword({
    required String currentPassword,
    required String newPassword,
  }) async {
    await api.post('/api/me/profile/password', body: {
      'currentPassword': currentPassword,
      'newPassword': newPassword,
    });
  }
}