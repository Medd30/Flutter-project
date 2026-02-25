import 'package:flutter/material.dart';
import '../data/profile_api.dart';

class ProfileProvider extends ChangeNotifier {
  final ProfileApi api;
  ProfileProvider({required this.api});

  bool loading = false;
  String? error;

  String? username;
  String? email;
  String? phone;

  Future<void> load() async {
    loading = true;
    error = null;
    notifyListeners();
    try {
      final res = await api.me();
      username = res['username']?.toString();
      email = res['email']?.toString();
      phone = res['phone']?.toString();
    } catch (e) {
      error = e.toString().replaceAll('Exception: ', '');
    } finally {
      loading = false;
      notifyListeners();
    }
  }

  Future<void> updateUsername(String newUsername) async {
    loading = true;
    error = null;
    notifyListeners();
    try {
      final res = await api.updateUsername(newUsername.trim());
      username = res['username']?.toString();
    } catch (e) {
      error = e.toString().replaceAll('Exception: ', '');
      rethrow;
    } finally {
      loading = false;
      notifyListeners();
    }
  }

  Future<void> changePassword(String current, String next) async {
    loading = true;
    error = null;
    notifyListeners();
    try {
      await api.changePassword(currentPassword: current, newPassword: next);
    } catch (e) {
      error = e.toString().replaceAll('Exception: ', '');
      rethrow;
    } finally {
      loading = false;
      notifyListeners();
    }
  }
}