import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../data/auth_api_provider.dart';
import 'login_screen.dart';

class VerifyScreen extends StatefulWidget {
  final int userId;

  const VerifyScreen({
    super.key,
    required this.userId,
  });

  @override
  State<VerifyScreen> createState() => _VerifyScreenState();
}

class _VerifyScreenState extends State<VerifyScreen> {
  final codeC = TextEditingController();
  bool loading = false;
  String? error;

  Future<void> _verify() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final auth = context.read<AuthApiProvider>().api;

      await auth.verifyEmail(
        userId: widget.userId,
        code: codeC.text.trim(),
      );

      if (!mounted) return;

      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Email verified. Wait for admin approval."),
        ),
      );
    } catch (e) {
      setState(() => error = e.toString().replaceAll('Exception: ', ''));
    } finally {
      setState(() => loading = false);
    }
  }

  @override
  void dispose() {
    codeC.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Verify Email")),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              TextField(
                controller: codeC,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: "Verification Code",
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 16),

              if (error != null)
                Text(error!, style: const TextStyle(color: Colors.red)),

              const SizedBox(height: 16),

              FilledButton(
                onPressed: loading ? null : _verify,
                child: loading
                    ? const SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      )
                    : const Text("Verify"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}