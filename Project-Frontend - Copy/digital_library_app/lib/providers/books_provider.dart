import 'dart:async';
import 'package:flutter/material.dart';
import '../data/books_api.dart';
import '../models/book.dart';

class BooksProvider extends ChangeNotifier {
  final BooksApi booksApi;

  BooksProvider({required this.booksApi});

  bool loading = false;        // initial load / full refresh
  bool searching = false;      // searching while keeping current list visible
  String? error;

  List<Book> books = [];

  String _search = '';
  String _category = '';

  Timer? _debounce;

  String get search => _search;
  String get category => _category;

  bool get hasActiveFilters =>
      _search.trim().isNotEmpty || _category.trim().isNotEmpty;

  // Called when app opens Home the first time
  Future<void> loadInitial() async {
    loading = true;
    error = null;
    notifyListeners();

    try {
      books = await booksApi.listBooks();
    } catch (e) {
      error = e.toString();
    } finally {
      loading = false;
      notifyListeners();
    }
  }

  // Called when user types
 void setSearch(String value) {
  _search = value;

  _debounce?.cancel();
  _debounce = Timer(const Duration(milliseconds: 400), () {
    loadFiltered(keepOldWhileLoading: true);
  });

  notifyListeners();
}

  void setCategory(String value) {
    _category = value;
    loadFiltered(keepOldWhileLoading: true);
    notifyListeners();
  }

  void clearFilters() {
    _search = '';
    _category = '';
    loadFiltered(keepOldWhileLoading: true);
    notifyListeners();
  }

  Future<void> loadFiltered({bool keepOldWhileLoading = true}) async {
    error = null;

    // If we keep old list, show small "searching" indicator (not full screen loader)
    if (keepOldWhileLoading) {
      searching = true;
    } else {
      loading = true;
    }
    notifyListeners();

    try {
      books = await booksApi.listBooks(
        search: _search.trim(),
        category: _category.trim(),
      );
    } catch (e) {
      error = e.toString();
    } finally {
      searching = false;
      loading = false;
      notifyListeners();
    }
  }

  @override
  void dispose() {
    _debounce?.cancel();
    super.dispose();
  }
}