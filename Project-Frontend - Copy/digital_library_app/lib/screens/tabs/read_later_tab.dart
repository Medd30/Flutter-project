import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../data/books_api_provider.dart';
import '../../providers/book_list_provider.dart';
import '../../core/app_config.dart';
import 'book_details_screen.dart';

class ReadLaterTab extends StatefulWidget {
  const ReadLaterTab({super.key});

  @override
  State<ReadLaterTab> createState() => _ReadLaterTabState();
}

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

class _ReadLaterTabState extends State<ReadLaterTab> {
  @override
  Widget build(BuildContext context) {
    final api = context.read<BooksApiProvider>().api;

    return ChangeNotifierProvider(
      create: (_) => BookListProvider(loader: api.myReadLater)..load(),
      child: const _ReadLaterView(),
    );
  }
}

class _ReadLaterView extends StatelessWidget {
  const _ReadLaterView();

  @override
  Widget build(BuildContext context) {
    final state = context.watch<BookListProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Read Later')),
      body: SafeArea(
        child: state.books.isEmpty
            ? const Center(child: Text('No read later books yet.'))
            : ListView.separated(
                padding: const EdgeInsets.all(16),
                itemCount: state.books.length,
                separatorBuilder: (_, _) => const SizedBox(height: 12),
                itemBuilder: (context, i) {
                  final b = state.books[i];

                  final cover = (b.coverImageUrl == null || b.coverImageUrl!.isEmpty)
                      ? null
                      : _resolveUrl(b.coverImageUrl!);

                  return _ReadLaterTile(
                    title: b.title,
                    coverUrl: cover,
                    subtitle: b.category,
                    onTap: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(builder: (_) => BookDetailsScreen(book: b)),
                      );
                    },
                  );
                },
              ),
      ),
    );
  }
}

class _ReadLaterTile extends StatelessWidget {
  final String title;
  final String? subtitle;
  final String? coverUrl;
  final VoidCallback onTap;

  const _ReadLaterTile({
    required this.title,
    required this.subtitle,
    required this.coverUrl,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final hasCover = coverUrl != null && coverUrl!.isNotEmpty;

    return InkWell(
      borderRadius: BorderRadius.circular(18),
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(18),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.06),
              blurRadius: 18,
              offset: const Offset(0, 10),
            )
          ],
        ),
        child: Row(
          children: [
            ClipRRect(
              borderRadius: BorderRadius.circular(14),
              child: Container(
                width: 60,
                height: 80,
                color: Colors.black.withOpacity(0.04),
                child: !hasCover
                    ? const Icon(Icons.menu_book_rounded)
                    : Image.network(
                        coverUrl!,
                        fit: BoxFit.cover,
                        errorBuilder: (_, _, _) =>
                            const Icon(Icons.broken_image_outlined),
                      ),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(title,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(fontWeight: FontWeight.w800)),
                  const SizedBox(height: 4),
                  Text(
                    subtitle ?? '',
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(color: Colors.black.withOpacity(0.6)),
                  ),
                ],
              ),
            ),
            const Icon(Icons.chevron_right),
          ],
        ),
      ),
    );
  }
}