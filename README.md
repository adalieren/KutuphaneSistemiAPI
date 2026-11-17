# Kütüphane Yönetim Sistemi REST API (KutuphaneSistemiAPI)

Bu proje, bir üniversite kütüphanesinin öğrencilerin kitap ödünç alma süreçlerini dijitalleştirmek amacıyla geliştirilmiş bir **REST API** uygulamasıdır. Proje, nesne tabanlı programlama (OOP) ilkelerine ve **Katmanlı Mimari (Layered Architecture)** prensiplerine uygun olarak **Java (Spring Boot)** ve **Gradle** kullanılarak geliştirilmiştir. Veri saklama katmanı olarak **PostgreSQL** kullanılmıştır.

Sistem, hem **kütüphane görevlileri (Librarian)** hem de **öğrencilerin (Student)** sistemi yetkileri dahilinde kullanabilmesi için **Spring Security** ile koruma altına alınmış ve **HTTP Basic Authentication** kullanılarak rol tabanlı yetkilendirme (Role-Based Access Control) yapısı kurulmuştur.

---

## Teknolojiler ve Kütüphaneler
*   **Java 17/21/23**
*   **Spring Boot 3.x**
*   **Spring Security 6.x** (Basic Authentication)
*   **Spring Data JPA** (Hibernate)
*   **PostgreSQL Driver**
*   **Spring Boot Validation** (Girdi doğrulama için)
*   **Gradle** (Proje ve bağımlılık yönetimi için)

---

## Proje Katmanlı Mimari Yapısı
Proje, sorumlulukların ayrılması ilkesine (Separation of Concerns) uygun olarak 4 ana katmandan oluşmaktadır:
1.  **Model (Entity) Katmanı (`com.library.management.model`)**: Veritabanı şemasını ve nesne yapılarını temsil eder. validation anatasyonları ile girdi doğrulamaları bu katmandadır.
    *   `Student`: Öğrenci bilgilerini tutar. `User` ile `@OneToOne` ilişkilidir.
    *   `Book`: Kitap bilgilerini ve kütüphanede mevcut olup olmadığını (`available`) tutar.
    *   `Loan`: Öğrenci ile Kitap arasındaki ilişkisel ödünç alma işlemlerini ve tarihlerini tutar.
    *   `User`: Kullanıcı adı, şifre (`BCrypt` şifrelenmiş) ve `Role` bilgilerini tutar.
2.  **DTO (Veri Transfer Nesneleri) Katmanı (`com.library.management.dto`)**: API istemcisine sunulan verilerin biçimlendirildiği ve filtrelendiği katmandır. Hassas verileri (kullanıcı şifre hash'leri vb.) gizler ve JSON serileştirme esnasındaki döngüsel bağımlılıkları (circular references) engeller.
3.  **Repository Katmanı (`com.library.management.repository`)**: Veritabanı üzerinde CRUD (Ekleme, Okuma, Güncelleme, Silme) ve özel sorgu işlemlerini yürüten Spring Data JPA arayüzleridir.
4.  **Service Katmanı (`com.library.management.service`)**: İş kurallarının (Business Logic) denetlendiği yerdir. Örneğin, ödünç alınmış bir kitabın tekrar ödünç verilememesi veya üzerinde aktif ödünç kaydı bulunan öğrencilerin silinememesi gibi kurallar burada işletilir.
5.  **Controller Katmanı (`com.library.management.controller`)**: Dış dünyaya RESTful HTTP uç noktaları (Endpoints) sunan API katmanıdır.

---

## Kimlik Doğrulama ve Giriş Bilgileri (Security & Roles)

Uygulama başlatıldığında, veritabanına otomatik olarak bir adet varsayılan kütüphane görevlisi hesabı tanımlanır. Yeni öğrenciler kaydedildiğinde ise sistem onlar adına otomatik olarak öğrenci hesabı oluşturur.

### 1. Kütüphane Görevlisi (Librarian) Rolü
*   **Yetkileri:** Kitap ve öğrenci ekleyebilir, silebilir, güncelleyebilir. Kitap ödünç verme ve iade alma süreçlerini yönetebilir. Tüm listeleme uç noktalarına erişebilir.
*   **Varsayılan Kullanıcı Adı:** `admin`
*   **Varsayılan Şifre:** `admin`

### 2. Öğrenci (Student) Rolü
*   **Yetkileri:** Sadece kütüphanedeki kitapları listeleyebilir, kitap detaylarını görebilir ve kendi aldığı kitapların listesine (ödünç geçmişine) erişebilir. Kitap ekleyemez, güncelleyemez, başkalarının bilgilerini sorgulamamaz (`403 Forbidden` alır).
*   **Kullanıcı Adı:** Kaydedilen öğrencinin **Öğrenci Numarası** (`studentNumber`)
*   **Varsayılan Şifre:** Öğrencinin **Öğrenci Numarası** (`studentNumber`)

---


---

## API İstek Uç Noktaları ve Kullanımı (Postman & REST Client Uyumlu)

Tüm isteklerde **HTTP Basic Authentication** aktiftir. İsteklerinizi gönderirken ilgili giriş bilgilerini eklemeyi unutmayınız.

### 1. Öğrenci İşlemleri (Sadece LIBRARIAN Yetkilidir)

#### Yeni Öğrenci Kaydetme
*   **Metot:** `POST`
*   **Adres:** `http://localhost:8080/api/students`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **İstek Başlığı:** `Content-Type: application/json`
*   **İstek Gövdesi (JSON):**
    ```json
    {
      "studentNumber": "20240101",
      "name": "Ahmet Yılmaz",
      "email": "ahmet.yilmaz@galata.edu.tr"
    }
    ```

#### Tüm Öğrencileri Listeleme
*   **Metot:** `GET`
*   **Adres:** `http://localhost:8080/api/students`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)

#### ID ile Öğrenci Detayı Getirme
*   **Metot:** `GET`
*   **Adres:** `http://localhost:8080/api/students/1` (Sondaki `1` öğrenci ID'sidir)
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN) veya `20240101` : `20240101` (STUDENT)

#### Öğrenci Güncelleme
*   **Metot:** `PUT`
*   **Adres:** `http://localhost:8080/api/students/1`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **İstek Başlığı:** `Content-Type: application/json`
*   **İstek Gövdesi (JSON):**
    ```json
    {
      "studentNumber": "20240101",
      "name": "Ahmet Yılmaz (Güncellendi)",
      "email": "ahmet.yeni@galata.edu.tr"
    }
    ```

#### Öğrenci Silme
*   **Metot:** `DELETE`
*   **Adres:** `http://localhost:8080/api/students/1`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)

---

### 2. Kitap İşlemleri

#### Yeni Kitap Ekleme (Sadece LIBRARIAN Yetkilidir)
*   **Metot:** `POST`
*   **Adres:** `http://localhost:8080/api/books`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **İstek Başlığı:** `Content-Type: application/json`
*   **İstek Gövdesi (JSON):**
    ```json
    {
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "isbn": "978-0132350884"
    }
    ```

#### Tüm Kitapları Listeleme (LIBRARIAN ve STUDENT Yetkilidir)
*   **Metot:** `GET`
*   **Adres:** `http://localhost:8080/api/books`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN) **veya** `20240101` : `20240101` (STUDENT)

#### Kitap Güncelleme (Sadece LIBRARIAN Yetkilidir)
*   **Metot:** `PUT`
*   **Adres:** `http://localhost:8080/api/books/1`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **İstek Başlığı:** `Content-Type: application/json`
*   **İstek Gövdesi (JSON):**
    ```json
    {
      "title": "Clean Code (Genişletilmiş)",
      "author": "Robert C. Martin",
      "isbn": "978-0132350884"
    }
    ```

#### Kitap Silme (Sadece LIBRARIAN Yetkilidir)
*   **Metot:** `DELETE`
*   **Adres:** `http://localhost:8080/api/books/1`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)

---

### 3. Ödünç Alma & İade İşlemleri (Sadece LIBRARIAN Yetkilidir)

#### Kitap Ödünç Verme
*   **Metot:** `POST`
*   **Adres:** `http://localhost:8080/api/loans/borrow`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **Sorgu Parametreleri (Query Params):**
    *   `studentId` = `1` (Öğrenci benzersiz ID'si)
    *   `bookId` = `1` (Kitap benzersiz ID'si)
*   **Adres Satırı Görünümü:** `http://localhost:8080/api/loans/borrow?studentId=1&bookId=1`

#### Kitap İade Etme
*   **Metot:** `POST`
*   **Adres:** `http://localhost:8080/api/loans/return`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
*   **Sorgu Parametreleri (Query Params):**
    *   `studentId` = `1`
    *   `bookId` = `1`
*   **Adres Satırı Görünümü:** `http://localhost:8080/api/loans/return?studentId=1&bookId=1`

---

### 4. İlişkisel Sorgular

#### Öğrencinin Ödünç Aldığı Tüm Kitapları Listeleme (LIBRARIAN ve STUDENT Yetkilidir)
*   **Metot:** `GET`
*   **Adres:** `http://localhost:8080/api/students/1/books`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN) **veya** `20240101` : `20240101` (STUDENT)

#### Kitabın Ödünç Geçmişini (Kimler Tarafından Alındığını) Görme (Sadece LIBRARIAN Yetkilidir)
*   **Metot:** `GET`
*   **Adres:** `http://localhost:8080/api/books/1/students`
*   **Giriş Yetkisi:** `admin` : `admin` (LIBRARIAN)
