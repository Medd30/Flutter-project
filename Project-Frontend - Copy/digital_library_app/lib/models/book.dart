class Book {
  final int id;
  final String title;
  final String? author;
  final String? category;
  final String? coverImageUrl;
  final String? pdfUrl;

  Book({
    required this.id,
    required this.title,
    this.author,
    this.category,
    this.coverImageUrl,
    this.pdfUrl,
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      id: (json['id'] as num).toInt(),
      title: (json['title'] ?? '').toString(),
      author: json['author']?.toString(),
      category: json['category']?.toString(),
      coverImageUrl: json['coverImageUrl']?.toString(),
      pdfUrl: json['pdfUrl']?.toString(),
    );
  }
}
