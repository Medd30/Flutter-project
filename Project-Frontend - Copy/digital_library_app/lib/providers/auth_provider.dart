import 'package:flutter/material.dart';
import '../data/auth_api.dart';
import '../data/token_store.dart';

class AuthProvider extends ChangeNotifier {
  final AuthApi authApi;
  final TokenStore tokenStore;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  String? _token;
  String? get token => _token;

  String? _role;
  String? get role => _role;

  AuthProvider({required this.authApi, required this.tokenStore});

  Future<void> loadFromStorage() async {
    _token = await tokenStore.getToken();
    _role = await tokenStore.getRole();
    notifyListeners();
  }

  /// ✅ Better error handling for messages like:
  /// - "Account not verified yet"
  /// - "Account not approved yet"
  /// - "Invalid password"
  /// - etc.
  Future<void> login(String identifier, String password) async {
    _setLoading(true);
    try {
      final res = await authApi.login(identifier: identifier, password: password);

      final t = res['token']?.toString();
      final r = res['role']?.toString();

      if (t == null || t.isEmpty || r == null || r.isEmpty) {
        throw Exception('Invalid login response from server');
      }

      await tokenStore.saveToken(t, r);
      _token = t;
      _role = r;
      notifyListeners();
    } catch (e) {
      // ✅ If login fails, make sure we are not stuck with an old token
      await tokenStore.clear();
      _token = null;
      _role = null;
      notifyListeners();

      // ✅ Re-throw a clean message (ApiClient usually returns a readable message)
      throw Exception(_prettyError(e));
    } finally {
      _setLoading(false);
    }
  }

  Future<void> logout() async {
    await tokenStore.clear();
    _token = null;
    _role = null;
    notifyListeners();
  }

  void _setLoading(bool v) {
    _isLoading = v;
    notifyListeners();
  }

  String _prettyError(Object e) {
    final s = e.toString();

    // Removes "Exception: " prefix if present
    if (s.startsWith('Exception: ')) return s.substring('Exception: '.length);

    // If ApiClient already throws a clean string, keep it
    return s;
  }
}