import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/books_provider.dart';
import '../../core/app_config.dart';
import 'book_details_screen.dart';

class HomeTab extends StatefulWidget {
  const HomeTab({super.key});

  @override
  State<HomeTab> createState() => _HomeTabState();
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

class _HomeTabState extends State<HomeTab> {
  @override
  void initState() {
    super.initState();
    Future.microtask(() => context.read<BooksProvider>().loadInitial());
  }

  @override
  Widget build(BuildContext context) {
    final state = context.watch<BooksProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Library')),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              const _SearchBar(),
              const SizedBox(height: 8),
              if (state.searching) const LinearProgressIndicator(minHeight: 2),
              if (state.searching) const SizedBox(height: 8),
              Expanded(child: _buildBody(state)),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildBody(BooksProvider state) {
    if (state.loading && state.books.isEmpty) {
      return const Center(child: CircularProgressIndicator());
    }

    if (state.books.isEmpty) {
      return const Center(child: Text('No books yet.'));
    }

    // 🔥 2X BIGGER CARDS
    return GridView.builder(
      padding: const EdgeInsets.only(top: 6),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2, // was 4 → now 2
        crossAxisSpacing: 16,
        mainAxisSpacing: 16,
        childAspectRatio: 0.72,
      ),
      itemCount: state.books.length,
      itemBuilder: (context, i) {
        final b = state.books[i];

        final cover = (b.coverImageUrl == null || b.coverImageUrl!.isEmpty)
            ? null
            : _resolveUrl(b.coverImageUrl!);

        return _BookCard(
          title: b.title,
          coverUrl: cover,
          onTap: () {
            Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => BookDetailsScreen(book: b)),
            );
          },
        );
      },
    );
  }
}

class _SearchBar extends StatelessWidget {
  const _SearchBar();

  @override
  Widget build(BuildContext context) {
    final p = context.read<BooksProvider>();

    return TextField(
      onChanged: p.setSearch,
      decoration: const InputDecoration(
        hintText: 'Search books...',
        prefixIcon: Icon(Icons.search),
      ),
    );
  }
}

class _BookCard extends StatelessWidget {
  final String title;
  final String? coverUrl;
  final VoidCallback onTap;

  const _BookCard({
    required this.title,
    required this.coverUrl,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final hasCover = coverUrl != null && coverUrl!.isNotEmpty;

    return InkWell(
      borderRadius: BorderRadius.circular(22),
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(22),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.08),
              blurRadius: 20,
              offset: const Offset(0, 12),
            )
          ],
        ),
        child: Column(
          children: [
            Expanded(
              child: ClipRRect(
                borderRadius: const BorderRadius.vertical(top: Radius.circular(22)),
                child: !hasCover
                    ? const Center(child: Icon(Icons.menu_book_rounded, size: 42))
                    : Image.network(
                        coverUrl!,
                        fit: BoxFit.cover,
                        errorBuilder: (_, __, ___) =>
                            const Center(child: Icon(Icons.broken_image_outlined)),
                      ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(14),
              child: Text(
                title,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  fontWeight: FontWeight.w800,
                  fontSize: 15,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}