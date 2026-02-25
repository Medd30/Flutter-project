class BookDetails {
  final int id;
  final String title;
  final String? author;
  final String? category;
  final String? coverImageUrl;
  final String? pdfUrl;
  final double avgRating;
  final int reviewCount;
  final bool isFavorite;
  final bool isReadLater;

  BookDetails({
    required this.id,
    required this.title,
    this.author,
    this.category,
    this.coverImageUrl,
    this.pdfUrl,
    required this.avgRating,
    required this.reviewCount,
    required this.isFavorite,
    required this.isReadLater,
  });

  factory BookDetails.fromJson(Map<String, dynamic> json) {
    return BookDetails(
      id: (json['id'] as num).toInt(),
      title: (json['title'] ?? '').toString(),
      author: json['author']?.toString(),
      category: json['category']?.toString(),
      coverImageUrl: json['coverImageUrl']?.toString(),
      pdfUrl: json['pdfUrl']?.toString(),
      avgRating: (json['avgRating'] as num?)?.toDouble() ?? 0.0,
      reviewCount: (json['reviewCount'] as num?)?.toInt() ?? 0,
      isFavorite: json['isFavorite'] == true,
      isReadLater: json['isReadLater'] == true,
    );
  }
}
