// class Review {
//   final int id;
//   final int rating;
//   final String? comment;
//   final String username;
//   final bool mine;
//   final DateTime? createdAt;

//   Review({
//     required this.id,
//     required this.rating,
//     required this.username,
//     required this.mine,
//     this.comment,
//     this.createdAt,
//   });

//   factory Review.fromJson(Map<String, dynamic> json) {
//     return Review(
//       id: (json['id'] as num).toInt(),
//       rating: (json['rating'] as num).toInt(),
//       comment: json['comment'] as String?,
//       username: (json['username'] ?? '') as String,
//       mine: (json['mine'] ?? false) as bool,
//       createdAt: json['createdAt'] == null ? null : DateTime.tryParse(json['createdAt']),
//     );
//   }
// }