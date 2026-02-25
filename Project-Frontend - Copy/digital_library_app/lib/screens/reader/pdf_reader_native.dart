import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';

class PdfReaderImpl extends StatefulWidget {
  final String title;
  final String pdfUrl;

  const PdfReaderImpl({super.key, required this.title, required this.pdfUrl});

  @override
  State<PdfReaderImpl> createState() => _PdfReaderImplState();
}

class _PdfReaderImplState extends State<PdfReaderImpl> {
  final PdfViewerController _controller = PdfViewerController();
  int _pageNumber = 0;
  int _pageCount = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title, maxLines: 1, overflow: TextOverflow.ellipsis),
        actions: [
          if (_pageCount > 0)
            Center(
              child: Padding(
                padding: const EdgeInsets.only(right: 12),
                child: Text(
                  '${_pageNumber + 1} / $_pageCount',
                  style: const TextStyle(fontWeight: FontWeight.w700),
                ),
              ),
            ),
        ],
      ),
      body: SfPdfViewer.network(
        widget.pdfUrl,
        controller: _controller,
        pageLayoutMode: PdfPageLayoutMode.single,
        scrollDirection: PdfScrollDirection.horizontal,
        onDocumentLoaded: (d) => setState(() => _pageCount = d.document.pages.count),
        onPageChanged: (d) => setState(() => _pageNumber = d.newPageNumber - 1),
        canShowScrollHead: false,
      ),
    );
  }
}
