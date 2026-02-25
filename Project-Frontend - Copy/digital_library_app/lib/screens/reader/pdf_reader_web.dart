import 'dart:html' as html;
import 'dart:ui_web' as ui;

import 'package:flutter/material.dart';

class PdfReaderImpl extends StatefulWidget {
  final String title;
  final String pdfUrl;

  const PdfReaderImpl({
    super.key,
    required this.title,
    required this.pdfUrl,
  });

  @override
  State<PdfReaderImpl> createState() => _PdfReaderImplState();
}

class _PdfReaderImplState extends State<PdfReaderImpl> {
  late final String _viewType;

  @override
  void initState() {
    super.initState();

    _viewType = 'pdf-iframe-${DateTime.now().microsecondsSinceEpoch}';

    ui.platformViewRegistry.registerViewFactory(_viewType, (int viewId) {
      final iframe = html.IFrameElement()
        ..src = widget.pdfUrl
        ..style.border = 'none'
        ..style.width = '100%'
        ..style.height = '100%';

      return iframe;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title, maxLines: 1, overflow: TextOverflow.ellipsis),
      ),
      body: HtmlElementView(viewType: _viewType),
    );
  }
}
