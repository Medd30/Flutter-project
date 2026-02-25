import '../models/book.dart';
import '../models/book_details.dart';
import 'api_client.dart';
// import '../models/review.dart';

class BooksApi {
  final ApiClient api;
  BooksApi(this.api);

 Future<List<Book>> listBooks({String? search, String? category}) async {
  final query = <String, String>{};

  if (search != null && search.trim().isNotEmpty) {
    query['search'] = search.trim();
  }
  if (category != null && category.trim().isNotEmpty) {
    query['category'] = category.trim();
  }

  final path = query.isEmpty
      ? '/api/books'
      : '/api/books?${Uri(queryParameters: query).query}';

  final res = await api.get(path);
  final list = (res as List).cast<dynamic>();
  return list.map((e) => Book.fromJson(e as Map<String, dynamic>)).toList();
}

  Future<BookDetails> getDetails(int bookId) async {
    final res = await api.get('/api/books/$bookId/details');
    return BookDetails.fromJson(res as Map<String, dynamic>);
  }

  Future<void> addFavorite(int bookId) async {
    await api.post('/api/me/favorites/$bookId');
  }

  Future<void> removeFavorite(int bookId) async {
    await api.delete('/api/me/favorites/$bookId');
  }

  Future<void> addReadLater(int bookId) async {
    await api.post('/api/me/read-later/$bookId');
  }

  Future<void> removeReadLater(int bookId) async {
    await api.delete('/api/me/read-later/$bookId');
  }

  Future<List<Book>> myFavorites() async {
    final res = await api.get('/api/me/favorites');
    final list = (res as List).cast<dynamic>();
    return list.map((e) => Book.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<List<Book>> myReadLater() async {
    final res = await api.get('/api/me/read-later');
    final list = (res as List).cast<dynamic>();
    return list.map((e) => Book.fromJson(e as Map<String, dynamic>)).toList();
  }

  Future<Map<String, dynamic>> getProgress(int bookId) async {
  final res = await api.get('/api/me/progress/$bookId');
  return res as Map<String, dynamic>;
}

Future<Map<String, dynamic>> saveProgress(int bookId, int lastPage, double percentage) async {
  final res = await api.post(
    '/api/me/progress/$bookId',
    body: {
      "lastPage": lastPage,
      "percentage": percentage,
    },
  );
  return res as Map<String, dynamic>;
}


// Future<List<Review>> listReviews(int bookId) async {
//   final res = await api.get('/api/books/$bookId/reviews');
//   final list = (res as List).cast<dynamic>();
//   return list.map((e) => Review.fromJson(e as Map<String, dynamic>)).toList();
// }

// Future<Review> upsertReview(int bookId, int rating, String? comment) async {
//   final res = await api.post(
//     '/api/books/$bookId/reviews',
//     body: {
//       'rating': rating,
//       'comment': comment,
//     },
//   );
//   return Review.fromJson(res as Map<String, dynamic>);
// }

// Future<Review> upsertReview(int bookId, int rating, String? comment) async {
//   final body = <String, dynamic>{'rating': rating};

//   final c = comment?.trim();
//   if (c != null && c.isNotEmpty) {
//     body['comment'] = c;
//   }

//   final res = await api.post(
//     '/api/books/$bookId/reviews',
//     body: body,
//   );
//   return Review.fromJson(res as Map<String, dynamic>);
// }

Future<void> deleteMyReview(int bookId) async {
  await api.delete('/api/books/$bookId/reviews/me');
}

}
