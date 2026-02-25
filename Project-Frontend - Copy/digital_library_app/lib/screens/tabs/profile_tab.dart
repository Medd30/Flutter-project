import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../providers/auth_provider.dart';
import '../../providers/profile_provider.dart';
import '../profile/change_password_screen.dart';
import '../profile/change_username_screen.dart';

class ProfileTab extends StatefulWidget {
  const ProfileTab({super.key});

  @override
  State<ProfileTab> createState() => _ProfileTabState();
}

class _ProfileTabState extends State<ProfileTab> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => context.read<ProfileProvider>().load());
  }

  @override
  Widget build(BuildContext context) {
    final p = context.watch<ProfileProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text("Profile")),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: p.loading
              ? const Center(child: CircularProgressIndicator())
              : p.error != null
                  ? Center(child: Text(p.error!))
                  : Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        _HelloCard(username: p.username ?? "User"),

                        const SizedBox(height: 14),

                        FilledButton(
                          onPressed: () async {
                            await Navigator.of(context).push(
                              MaterialPageRoute(builder: (_) => const ChangeUsernameScreen()),
                            );
                            // refresh after coming back
                            if (!mounted) return;
                            await context.read<ProfileProvider>().load();
                          },
                          child: const Text("Change Username"),
                        ),

                        const SizedBox(height: 10),

                        FilledButton(
                          onPressed: () async {
                            await Navigator.of(context).push(
                              MaterialPageRoute(builder: (_) => const ChangePasswordScreen()),
                            );
                          },
                          child: const Text("Change Password"),
                        ),

                        const Spacer(),

                        FilledButton.tonal(
                          onPressed: () async {
                            await context.read<AuthProvider>().logout();
                          },
                          child: const Text("Logout"),
                        ),
                      ],
                    ),
        ),
      ),
    );
  }
}

class _HelloCard extends StatelessWidget {
  final String username;
  const _HelloCard({required this.username});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(18),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [Color(0xFF6D28D9), Color(0xFF06B6D4)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(22),
      ),
      child: Text(
        "Hello, $username 👋",
        style: const TextStyle(
          color: Colors.white,
          fontSize: 20,
          fontWeight: FontWeight.w800,
        ),
      ),
    );
  }
}