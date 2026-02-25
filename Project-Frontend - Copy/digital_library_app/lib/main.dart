import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'core/app_theme.dart';
import 'core/app_config.dart';
import 'data/api_client.dart';
import 'data/token_store.dart';
import 'data/auth_api.dart';
import 'providers/auth_provider.dart';
import 'screens/auth/login_screen.dart';
import 'screens/tabs/tabs_shell.dart';

import 'data/books_api.dart';
import 'providers/books_provider.dart';
import 'data/books_api_provider.dart';

import 'data/profile_api.dart';
import 'providers/profile_provider.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const DigitalLibraryApp());
}

class DigitalLibraryApp extends StatelessWidget {
  const DigitalLibraryApp({super.key});

  @override
  Widget build(BuildContext context) {
    final tokenStore = TokenStore();

    final apiClient = ApiClient(
      baseUrl: AppConfig.baseUrl,
      tokenStore: tokenStore,
    );

    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => AuthProvider(
            authApi: AuthApi(apiClient),
            tokenStore: tokenStore,
          )..loadFromStorage(),
        ),

        ChangeNotifierProvider(
          create: (_) => BooksProvider(
            booksApi: BooksApi(apiClient),
          ),
        ),

        Provider(create: (_) => BooksApiProvider(BooksApi(apiClient))),

        ChangeNotifierProvider(
          create: (_) => ProfileProvider(
            api: ProfileApi(apiClient),
          ),
        ),
      ],
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: AppTheme.light(),
        home: const RootRouter(),
      ),
    );
  }
}

class RootRouter extends StatelessWidget {
  const RootRouter({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();

    if (auth.token == null) {
      return const LoginScreen();
    }
    return const TabsShell();
  }
}