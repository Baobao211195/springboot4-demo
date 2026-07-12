# Spring Boot & Kafka Demo Project

## 1. Tổng Quan

Đây là một dự án Spring Boot đơn giản nhằm minh họa cách tích hợp với Apache Kafka. Dự án xây dựng một REST API, khi được gọi, sẽ gửi một message tới một topic Kafka. Một consumer trong cùng ứng dụng sẽ lắng nghe và nhận message đó.

Dự án này cũng bao gồm một bộ lọc (filter) để ghi log cho mỗi request và response, giúp cho việc gỡ lỗi và theo dõi.

## 2. Cấu Trúc Project

Dưới đây là mô tả về các thành phần chính trong project:

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── consumer
│   │   │               │   ├── HelloConsumer.java        # (1) Kafka Consumer
│   │   │               │   ├── KafkaProducerService.java # (2) Kafka Producer
│   │   │               │   └── KafkaTopicConfig.java     # (3) Cấu hình Topic
│   │   │               ├── HelloController.java          # (4) REST API Controller
│   │   │               ├── HelloService.java             # (5) Lớp Service xử lý logic
│   │   │               ├── LoggingFilter.java            # (6) Bộ lọc log Request/Response
│   │   │               └── SpringAotDemoApplication.java # (7) Lớp Application chính
│   │   └── resources
│   │       └── application.yaml                          # (8) File cấu hình chính
│   └── ...
├── build.gradle                                          # File quản lý dependencies và build
└── ... (các file khác của project)
```

**Giải thích các thành phần:**

1.  **`HelloConsumer.java`**: Lắng nghe topic `demo-topic`. Khi có message mới, nó sẽ nhận và ghi log ra console.
2.  **`KafkaProducerService.java`**: Cung cấp một phương thức `sendMessage()` để gửi message (dưới dạng đối tượng `User`) đến topic `demo-topic`.
3.  **`KafkaTopicConfig.java`**: Tự động tạo topic `demo-topic` khi ứng dụng khởi động nếu nó chưa tồn tại.
4.  **`HelloController.java`**: Định nghĩa REST endpoint `GET /hello/{message}`. Đây là điểm khởi đầu của luồng xử lý. Nó cũng định nghĩa `record User` là kiểu dữ liệu cho message.
5.  **`HelloService.java`**: Lớp logic trung gian, được gọi bởi `HelloController` và sau đó gọi `KafkaProducerService` để gửi message.
6.  **`LoggingFilter.java`**: Một bộ lọc servlet, tự động ghi lại thông tin chi tiết về mỗi request (method, URI, payload) và response (status code, body, time taken).
7.  **`SpringAotDemoApplication.java`**: Lớp khởi động của ứng dụng Spring Boot.
8.  **`application.yaml`**: Chứa tất cả các cấu hình của ứng dụng, bao gồm thông tin kết nối Kafka, cấu hình producer/consumer (như serializer/deserializer), và cấu hình logging.

## 3. Cách Chạy Project

### Yêu Cầu
*   JDK 25
*   Docker và Docker Compose

### Các Bước Thực Hiện

1.  **Khởi động Kafka:**
    *   Mở terminal và di chuyển đến thư mục chứa file `docker-compose.yaml` của Kafka (ví dụ: `/Applications/namespace/cdc-consumer/`).
    *   Chạy lệnh sau để khởi động Kafka ở chế độ KRaft:
        ```sh
        docker-compose down -v
        docker-compose up -d
        ```

2.  **Chạy Ứng Dụng Spring Boot:**
    *   Mở project `springboot4-demo` trong IDE của bạn (ví dụ: IntelliJ).
    *   Chạy lớp `SpringAotDemoApplication.java`.

3.  **Trigger API:**
    *   Sử dụng trình duyệt hoặc một công cụ như `curl` để gọi API:
        ```sh
        curl http://localhost:8080/hello/world
        ```
    *   Bạn có thể thay `world` bằng bất kỳ chuỗi nào bạn muốn.

## 4. Luồng Hoạt Động

1.  Client gửi request `GET` đến `http://localhost:8080/hello/world`.
2.  `LoggingFilter` ghi log thông tin của request.
3.  `HelloController` nhận request, tạo một đối tượng `User` và gọi `helloService.sayHello()`.
4.  `HelloService` gọi `kafkaProducerService.sendMessage()` để gửi đối tượng `User` đi.
5.  `KafkaProducerService` sử dụng `KafkaTemplate` để serialize đối tượng `User` thành JSON và gửi đến topic `demo-topic`.
6.  `HelloConsumer` đang lắng nghe trên `demo-topic`, nhận được message JSON, deserialize nó trở lại thành đối tượng `User` và ghi log ra console.
7.  `LoggingFilter` ghi log thông tin của response trả về cho client.
