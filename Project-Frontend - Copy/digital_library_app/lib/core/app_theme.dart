import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class AppTheme {
  static ThemeData light() {
    final base = ThemeData.light();

    return base.copyWith(
      textTheme: GoogleFonts.poppinsTextTheme(base.textTheme),

      colorScheme: ColorScheme.fromSeed(
        seedColor: const Color(0xFF6D28D9),
        brightness: Brightness.light,
      ),

      scaffoldBackgroundColor: const Color(0xFFF7F7FB),

      appBarTheme: const AppBarTheme(
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),

      // 🔥 FIX HERE (CardThemeData instead of CardTheme)
      cardTheme: const CardThemeData(
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(18)),
        ),
      ),

      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(16),
          borderSide: BorderSide.none,
        ),
      ),
    );
  }
}
