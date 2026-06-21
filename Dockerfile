# ===== Giai đoạn 1: "Builder" - Sử dụng image đã có sẵn Tesseract =====
# Image này được lấy từ registry nội bộ của công ty và đã chứa Tesseract.
# Quá trình build sẽ không cần kết nối Internet để cài đặt nữa.
# !!! THAY THẾ bằng địa chỉ image nội bộ của bạn.
FROM your-company-registry.com/base/tesseract-ubuntu:1.0 AS tesseract_builder

# ===== Giai đoạn 2: "Final" - Image cuối cùng chứa ứng dụng của bạn =====
# Bắt đầu từ image Java quen thuộc của bạn.
FROM eclipse-temurin:21-jdk-jammy

# Đây là bước quan trọng: Sao chép Tesseract từ giai đoạn "builder" vào image cuối cùng.
# Việc này giống hệt ý tưởng của bạn là "copy từ thư mục đã chuẩn bị sẵn",
# nhưng đảm bảo các file được sao chép tương thích 100% với môi trường Linux.
COPY --from=tesseract_builder /usr/bin/tesseract /usr/bin/
COPY --from=tesseract_builder /usr/lib/x86_64-linux-gnu/ /usr/lib/x86_64-linux-gnu/
COPY --from=tesseract_builder /usr/share/tesseract-ocr/ /usr/share/tesseract-ocr/

# Sao chép file JAR của ứng dụng.
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Định nghĩa lệnh sẽ chạy khi container khởi động.
ENTRYPOINT ["java","-jar","/app.jar"]