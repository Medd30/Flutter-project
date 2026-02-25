import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/profile_provider.dart';

class ChangeUsernameScreen extends StatefulWidget {
  const ChangeUsernameScreen({super.key});

  @override
  State<ChangeUsernameScreen> createState() => _ChangeUsernameScreenState();
}

class _ChangeUsernameScreenState extends State<ChangeUsernameScreen> {
  final ctrl = TextEditingController();
  String? error;
  bool saving = false;

  @override
  void initState() {
    super.initState();
    final current = context.read<ProfileProvider>().username;
    ctrl.text = current ?? '';
  }

  @override
  void dispose() {
    ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Change Username")),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              TextField(
                controller: ctrl,
                decoration: const InputDecoration(
                  labelText: "New username",
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
                            await context.read<ProfileProvider>().updateUsername(ctrl.text);
                            if (!mounted) return;
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