import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../data/books_api_provider.dart';
import '../../models/book.dart';
import '../../providers/book_details_provider.dart';
import '../reader/pdf_reader_screen.dart';
import '../../core/app_config.dart';

class BookDetailsScreen extends StatelessWidget {
  final Book book;
  const BookDetailsScreen({super.key, required this.book});

  @override
  Widget build(BuildContext context) {
    final api = context.read<BooksApiProvider>().api;

    return ChangeNotifierProvider(
      create: (_) =>
          BookDetailsProvider(booksApi: api, bookId: book.id)..load(),
      child: const _BookDetailsView(),
    );
  }
}

/// Normalize URLs for real devices
String _resolveUrl(String raw) {
  final s = raw.trim();
  if (s.isEmpty) return s;

  if (s.startsWith('http://localhost:8080')) {
    return s.replaceFirst('http://localhost:8080', AppConfig.baseUrl);
  }
  if (s.startsWith('https://localhost:8080')) {
    return s.replaceFirst('https://localhost:8080', AppConfig.baseUrl);
  }

  if (s.startsWith('http://') || s.startsWith('https://')) return s;

  if (s.startsWith('/')) return '${AppConfig.baseUrl}$s';
  return '${AppConfig.baseUrl}/$s';
}

class _BookDetailsView extends StatelessWidget {
  const _BookDetailsView();

  @override
  Widget build(BuildContext context) {
    final state = context.watch<BookDetailsProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Book')),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: state.loading
              ? const Center(child: CircularProgressIndicator())
              : state.error != null
                  ? Center(child: Text(state.error!))
                  : const _Content(),
        ),
      ),
    );
  }
}

class _Content extends StatelessWidget {
  const _Content();

  @override
  Widget build(BuildContext context) {
    final state = context.watch<BookDetailsProvider>();
    final d = state.details!;

    final cover = (d.coverImageUrl == null || d.coverImageUrl!.isEmpty)
        ? null
        : _resolveUrl(d.coverImageUrl!);

    final pdf = (d.pdfUrl == null || d.pdfUrl!.isEmpty)
        ? null
        : _resolveUrl(d.pdfUrl!);

    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          _HeaderCard(
            title: d.title,
            coverUrl: cover,
            subtitle: [
              if (d.author != null && d.author!.isNotEmpty) d.author!,
              if (d.category != null && d.category!.isNotEmpty) d.category!,
            ].join(' • '),
            rating: d.avgRating,
            count: d.reviewCount,
          ),

          const SizedBox(height: 14),

          Row(
            children: [
              Expanded(
                child: FilledButton(
                  onPressed: (pdf == null || pdf.isEmpty)
                      ? null
                      : () {
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (_) => PdfReaderScreen(
                                title: d.title,
                                pdfUrl: pdf,
                              ),
                            ),
                          );
                        },
                  child: const Text('Read Book'),
                ),
              ),
              const SizedBox(width: 10),
              IconButton.filledTonal(
                onPressed: () async {
                  try {
                    await context
                        .read<BookDetailsProvider>()
                        .toggleFavorite();
                  } catch (_) {}
                },
                icon: Icon(
                  d.isFavorite
                      ? Icons.favorite
                      : Icons.favorite_border,
                ),
              ),
              const SizedBox(width: 8),
              IconButton.filledTonal(
                onPressed: () async {
                  try {
                    await context
                        .read<BookDetailsProvider>()
                        .toggleReadLater();
                  } catch (_) {}
                },
                icon: Icon(
                  d.isReadLater
                      ? Icons.bookmark
                      : Icons.bookmark_outline,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class _HeaderCard extends StatelessWidget {
  final String title;
  final String? coverUrl;
  final String subtitle;
  final double rating;
  final int count;

  const _HeaderCard({
    required this.title,
    required this.coverUrl,
    required this.subtitle,
    required this.rating,
    required this.count,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [Color(0xFF6D28D9), Color(0xFF06B6D4)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(22),
      ),
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(16),
            child: Container(
              width: 90,
              height: 120,
              color: Colors.white.withOpacity(0.18),
              child: (coverUrl == null || coverUrl!.isEmpty)
                  ? const Icon(Icons.menu_book_rounded,
                      color: Colors.white, size: 34)
                  : Image.network(
                      coverUrl!,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) =>
                          const Icon(Icons.broken_image_outlined,
                              color: Colors.white),
                    ),
            ),
          ),
          const SizedBox(width: 14),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 18,
                    fontWeight: FontWeight.w800,
                  ),
                ),
                const SizedBox(height: 6),
                if (subtitle.isNotEmpty)
                  Text(
                    subtitle,
                    style:
                        const TextStyle(color: Colors.white70),
                  ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    const Icon(Icons.star_rounded,
                        color: Colors.white),
                    const SizedBox(width: 6),
                    Text(
                      rating.toStringAsFixed(1),
                      style: const TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.w700),
                    ),
                    const SizedBox(width: 8),
                    Text(
                      '($count)',
                      style:
                          const TextStyle(color: Colors.white70),
                    ),
                  ],
                ),
              ],
            ),
          )
        ],
      ),
    );
  }
}