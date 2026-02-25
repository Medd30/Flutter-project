import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class TokenStore {
  static const _kToken = 'auth_token';
  static const _kRole = 'auth_role';

  final _storage = const FlutterSecureStorage();

  Future<void> saveToken(String token, String role) async {
    await _storage.write(key: _kToken, value: token);
    await _storage.write(key: _kRole, value: role);
  }

  Future<String?> getToken() => _storage.read(key: _kToken);
  Future<String?> getRole() => _storage.read(key: _kRole);

  Future<void> clear() async {
    await _storage.delete(key: _kToken);
    await _storage.delete(key: _kRole);
  }
}
