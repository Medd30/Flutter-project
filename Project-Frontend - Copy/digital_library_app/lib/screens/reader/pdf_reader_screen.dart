// ignore: unused_import
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:flutter/material.dart';

import 'pdf_reader_web.dart'
    if (dart.library.io) 'pdf_reader_native.dart';

class PdfReaderScreen extends StatelessWidget {
  final String title;
  final String pdfUrl;

  const PdfReaderScreen({
    super.key,
    required this.title,
    required this.pdfUrl,
  });

  @override
  Widget build(BuildContext context) {
    return PdfReaderImpl(title: title, pdfUrl: pdfUrl);
  }
}
