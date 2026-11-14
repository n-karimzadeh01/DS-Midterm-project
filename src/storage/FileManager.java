package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * مسئول خواندن و نوشتن فایل‌های CSV پروژه (users.csv و tickets.csv).
 *
 * طبق شرح پروژه:
 *  - فایل‌ها فقط یک‌بار در ابتدای اجرا خوانده می‌شوند (readCsv)
 *  - و یک‌بار در پایان اجرا، به‌صورت کامل بازنویسی می‌شوند (writeCsv)
 *  - در طول اجرای برنامه هیچ دسترسی مستقیمی به فایل نداریم.
 *
 * این کلاس عمداً از هیچ ساختار داده‌ای پروژه خبر ندارد: ردیف‌ها را به‌صورت
 * آرایهٔ رشته (String[]) تحویل می‌دهد و ردیف‌ها را به همین شکل می‌گیرد.
 * تبدیل ردیف به Account یا Ticket کارِ لایه‌های بالاتر است.
 *
 * محدودیت‌ها:
 *  - هر ردیف باید دقیقاً یک خط باشد؛ فیلدی که شکستن خط داشته باشد پذیرفته نمی‌شود.
 *  - این کلاس هیچ‌چیزی چاپ نمی‌کند و خطاها را با throw اعلام می‌کند.
 */
public class FileManager {

    /** برای هر ردیف داده (بدون سطر عنوان) یک‌بار صدا زده می‌شود. */
    public interface RowHandler {
        void handle(String[] fields);
    }

    /** آیا فایل وجود دارد؟ (مثلاً برای ساختن ادمین پیش‌فرض وقتی فایل نیست) */
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    /**
     * فایل CSV را می‌خواند، سطر عنوان را رد می‌کند و هر ردیف را به handler می‌دهد.
     * خط‌های خالی نادیده گرفته می‌شوند.
     */
    public void readCsv(Path path, RowHandler handler) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            // سطر اول عنوان ستون‌هاست و دور ریخته می‌شود.
            String line = reader.readLine();
            if (line == null) {
                return; // فایل کاملاً خالی است
            }
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                handler.handle(parseLine(line));
            }
        }
    }

    /**
     * فایل را از نو و کامل می‌نویسد (Overwrite): اول سطر عنوان، بعد همهٔ ردیف‌ها.
     * اگر پوشهٔ فایل وجود نداشته باشد ساخته می‌شود.
     */
    public void writeCsv(Path path, String[] header, Iterable<String[]> rows) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writeRow(writer, header);
            for (String[] row : rows) {
                writeRow(writer, row);
            }
        }
    }

    // ------------------------------------------------------------------
    // خواندن: شکستن یک خط به فیلدها
    // ------------------------------------------------------------------

    /**
     * یک خط CSV را به فیلدها می‌شکند. کاما داخل «"..."» جدا کنندهٔ فیلد حساب نمی‌شود
     * و دو علامت نقل قول پشت سر هم داخل «"..."» یعنی یک نقل قول واقعی.
     * فقط از length و charAt استفاده می‌کنیم (بدون split و substring).
     */
    private String[] parseLine(String line) {
        int length = line.length();

        // مرحلهٔ ۱: شمردن فیلدها (کاماهایی که بیرون از نقل قول‌اند، به‌علاوهٔ یک)
        int fieldCount = 1;
        boolean inQuotes = false;
        for (int i = 0; i < length; i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fieldCount++;
            }
        }

        // مرحلهٔ ۲: ساختن فیلدها. buffer فقط یک بافر موقت برای ساخت متن یک فیلد است
        // و هیچ دادهٔ ساختار داده‌ای در آن نگه‌داری نمی‌شود.
        String[] fields = new String[fieldCount];
        char[] buffer = new char[length];
        int bufferLength = 0;
        int index = 0;
        inQuotes = false;
        for (int i = 0; i < length; i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < length && line.charAt(i + 1) == '"') {
                        buffer[bufferLength++] = '"'; // نقل قول فرار داده‌شده
                        i++;
                    } else {
                        inQuotes = false;             // پایان فیلد نقل‌قول‌دار
                    }
                } else {
                    buffer[bufferLength++] = c;
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields[index++] = new String(buffer, 0, bufferLength);
                bufferLength = 0;
            } else {
                buffer[bufferLength++] = c;
            }
        }
        fields[index] = new String(buffer, 0, bufferLength);
        return fields;
    }

    // ------------------------------------------------------------------
    // نوشتن: ساخت یک خط از فیلدها
    // ------------------------------------------------------------------

    private void writeRow(BufferedWriter writer, String[] fields) throws IOException {
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escapeField(fields[i]));
        }
        writer.write('\n');
    }

    /**
     * اگر فیلد کاما یا نقل قول داشته باشد، آن را داخل «"..."» می‌گذارد و هر نقل قول
     * داخلش را دوتایی می‌کند، تا هنگام خواندن دوباره دقیقاً همان متن برگردد.
     * (مثلاً عنوان تیکتی که کاربر در کنسول می‌نویسد.)
     */
    private String escapeField(String field) {
        if (field == null) {
            throw new IllegalArgumentException("CSV field must not be null");
        }
        boolean needsQuotes = false;
        int quoteCount = 0;
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c == '\n' || c == '\r') {
                throw new IllegalArgumentException("CSV field must not contain a line break");
            }
            if (c == '"') {
                quoteCount++;
                needsQuotes = true;
            } else if (c == ',') {
                needsQuotes = true;
            }
        }
        if (!needsQuotes) {
            return field;
        }
        char[] out = new char[field.length() + quoteCount + 2];
        int n = 0;
        out[n++] = '"';
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c == '"') {
                out[n++] = '"';
            }
            out[n++] = c;
        }
        out[n++] = '"';
        return new String(out, 0, n);
    }
}