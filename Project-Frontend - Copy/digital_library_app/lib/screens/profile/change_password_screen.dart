import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/profile_provider.dart';

class ChangePasswordScreen extends StatefulWidget {
  const ChangePasswordScreen({super.key});

  @override
  State<ChangePasswordScreen> createState() => _ChangePasswordScreenState();
}

class _ChangePasswordScreenState extends State<ChangePasswordScreen> {
  final currentC = TextEditingController();
  final newC = TextEditingController();
  String? error;
  bool saving = false;

  @override
  void dispose() {
    currentC.dispose();
    newC.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Change Password")),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              TextField(
                controller: currentC,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: "Current password",
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: newC,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: "New password",
                  border: OutlineInputBorder(),
                ),
              ),
              if (error != null) ...[
                const SizedBox(height: 10),
                Text(error!, style: const TextStyle(color: Colors.red)),
              ],
              const SizedBox(height: 12),
              SizedBox(
                width: double.infinity,
                height: 50,
                child: FilledButton(
                  onPressed: saving
                      ? null
                      : () async {
                          setState(() {
                            error = null;
                            saving = true;
                          });
                          try {
                            await context.read<ProfileProvider>().changePassword(
                                  currentC.text,
                                  newC.text,
                                );
                            if (!mounted) return;
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(content: Text("Password changed")),
                            );
                            Navigator.of(context).pop();
                          } catch (e) {
                            setState(() => error = e.toString().replaceAll('Exception: ', ''));
                          } finally {
                            if (mounted) setState(() => saving = false);
                          }
                        },
                  child: saving
                      ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2))
                      : const Text("Save"),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}