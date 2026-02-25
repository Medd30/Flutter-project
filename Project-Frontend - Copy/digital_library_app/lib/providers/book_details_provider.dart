import 'package:flutter/material.dart';
import '../data/books_api.dart';
import '../models/book_details.dart';
//import '../models/review.dart';

class BookDetailsProvider extends ChangeNotifier {
  final BooksApi booksApi;
  final int bookId;

  BookDetailsProvider({required this.booksApi, required this.bookId});

  bool loading = false;
  String? error;
  BookDetails? details;

  // Reviews state
 // List<Review> reviews = [];
  bool reviewsLoading = false;
  String? reviewsError;

  // ---- Helpers
  // Review? get myReview {
  //   try {
  //     return reviews.firstWhere((r) => r.mine == true);
  //   } catch (_) {
  //     return null;
  //   }
  // }

  // bool get hasMyReview => myReview != null;

  Future<void> load() async {
    loading = true;
    error = null;
    notifyListeners();

    try {
      details = await booksApi.getDetails(bookId);
    } catch (e) {
      error = e.toString();
    } finally {
      loading = false;
      notifyListeners();
    }
  }

  // Future<void> loadReviews({bool silent = false}) async {
  //   if (reviewsLoading) return; // prevent double calls
  //   if (!silent) {
  //     reviewsLoading = true;
  //     reviewsError = null;
  //     notifyListeners();
  //   } else {
  //     reviewsLoading = true;
  //     reviewsError = null;
  //   }

  //   try {
  //     reviews = await booksApi.listReviews(bookId);
  //   } catch (e) {
  //     reviewsError = e.toString();
  //   } finally {
  //     reviewsLoading = false;
  //     if (!silent) notifyListeners();
  //   }
  // }

  // Future<void> submitReview(int rating, String? comment) async {
  //   // basic validation client-side
  //   if (rating < 1 || rating > 5) return;

  //   await booksApi.upsertReview(bookId, rating, comment);

  //   // refresh rating/count + list
  //   await load();
  //   // load() already calls loadReviews, but it’s fine either way.
  // }

  Future<void> deleteMyReview() async {
    await booksApi.deleteMyReview(bookId);
    await load();
  }

  Future<void> toggleFavorite() async {
    final d = details;
    if (d == null) return;

    final next = !d.isFavorite;

    // optimistic UI
    details = BookDetails(
      id: d.id,
      title: d.title,
      author: d.author,
      category: d.category,
      coverImageUrl: d.coverImageUrl,
      pdfUrl: d.pdfUrl,
      avgRating: d.avgRating,
      reviewCount: d.reviewCount,
      isFavorite: next,
      isReadLater: d.isReadLater,
    );
    notifyListeners();

    try {
      if (next) {
        await booksApi.addFavorite(bookId);
      } else {
        await booksApi.removeFavorite(bookId);
      }
    } catch (e) {
      await load(); // rollback
      rethrow;
    }
  }

  Future<void> toggleReadLater() async {
    final d = details;
    if (d == null) return;

    final next = !d.isReadLater;

    details = BookDetails(
      id: d.id,
      title: d.title,
      author: d.author,
      category: d.category,
      coverImageUrl: d.coverImageUrl,
      pdfUrl: d.pdfUrl,
      avgRating: d.avgRating,
      reviewCount: d.reviewCount,
      isFavorite: d.isFavorite,
      isReadLater: next,
    );
    notifyListeners();

    try {
      if (next) {
        await booksApi.addReadLater(bookId);
      } else {
        await booksApi.removeReadLater(bookId);
      }
    } catch (e) {
      await load(); // rollback
      rethrow;
    }
  }
}