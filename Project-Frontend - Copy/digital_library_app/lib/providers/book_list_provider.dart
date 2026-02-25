import 'package:flutter/material.dart';
import '../models/book.dart';

typedef BooksLoader = Future<List<Book>> Function();

class BookListProvider extends ChangeNotifier {
  final BooksLoader loader;

  BookListProvider({required this.loader});

  bool loading = false;
  String? error;
  List<Book> books = [];

  Future<void> load() async {
    loading = true;
    error = null;
    notifyListeners();
    try {
      books = await loader();
    } catch (e) {
      error = e.toString();
    } finally {
      loading = false;
      notifyListeners();
    }
  }
}
