-- V1__Initial_schema_setup.sql

-- ======== 1. جدول المستخدمين (الطلبة والدكاترة) ========
-- (نفس تصميم المرة السابقة)
CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    student_code  VARCHAR(50) UNIQUE,
    password      VARCHAR(255) NOT NULL,
    role          ENUM('STUDENT', 'DOCTOR') NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======== 2. جدول المحاضرات (الجدول الجديد) ========
CREATE TABLE lectures
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 'title' عنوان المحاضرة (مثل: "Chapter 1: Intro")
    title         VARCHAR(255) NOT NULL,

    -- 'description' وصف قصير للمحاضرة
    description   TEXT,

    -- 'doctor_id' ربط بالدكتور الذي أنشأ هذه المحاضرة
    doctor_id     BIGINT NOT NULL,

    -- 'is_active' هل المحاضرة مفتوحة الآن لاستقبال الحضور (لـ QR Code)
    is_active     BOOLEAN DEFAULT FALSE,

    -- 'created_at' تاريخ ووقت إنشاء المحاضرة
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (doctor_id) REFERENCES users(id)
);


-- ======== 3. جدول تسجيل الحضور (النسخة المعدلة) ========
CREATE TABLE attendance_records
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 'student_id' ربط بالطالب
    student_id        BIGINT NOT NULL,

    -- 'lecture_id' ربط بالمحاضرة (بدلاً من الأعمدة القديمة)
    lecture_id        BIGINT NOT NULL,

    -- 'status' هل هو حاضر أم غائب (الدكتور ممكن يعدلها)
    -- القيمة الافتراضية 'PRESENT' لأن الطالب عمل Scan بنفسه
    status            ENUM('PRESENT', 'ABSENT') NOT NULL DEFAULT 'PRESENT',

    -- 'notes' الملاحظات اللي الدكتور ممكن يكتبها
    notes             TEXT,

    -- 'recorded_at' الوقت الفعلي اللي الطالب عمل فيه Scan
    recorded_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraints
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (lecture_id) REFERENCES lectures(id) ON DELETE CASCADE,

    -- قيد (Constraint) يضمن عدم تكرار الحضور
    -- مينفعش نفس الطالب يحضر نفس المحاضرة مرتين
    UNIQUE KEY uk_student_lecture (student_id, lecture_id)
);

