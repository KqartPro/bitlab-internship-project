-- Создание таблицы Course
CREATE TABLE courses
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы Chapter
CREATE TABLE chapters
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    description   TEXT,
    created_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    chapter_order INT          NOT NULL,
    course_id     BIGINT,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

-- Создание таблицы Lesson
CREATE TABLE lessons
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    content      TEXT,
    lesson_order INT          NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    chapter_id   BIGINT,
    FOREIGN KEY (chapter_id) REFERENCES chapters (id) ON DELETE CASCADE
);


-- Courses
INSERT INTO courses (name, description, created_time, updated_time)
VALUES ('Алгоритмизация и программирование',
        'Введение в программирование на Java.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

INSERT INTO courses (name, description, created_time, updated_time)
VALUES ('Алгоритмы и структуры данных',
        'Введение в понятия алгоритмы и структуры данных',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- Chapters
INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Введение в Java',
        'Основы языка программирования Java.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1,
        1);

INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Условные Операторы',
        'Изучение If, Else, If-else конструкций',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2,
        1);

INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Циклы в Java',
        'Изучение циклов for, while, do-while',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        3,
        1);

INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Введение в понятия Алгоритмов и Структур данных в Java',
        'Основы Алгоритмов и Структур данных в Java.',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1,
        2);

INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Основные алгоритмы сортировки',
        'Изучение основных алгоритмов сортировки: Bubble, Merge, Quick...',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2,
        2);

INSERT INTO chapters (name, description, created_time, updated_time, chapter_order, course_id)
VALUES ('Основные структуры данных',
        'Изучение основных структур данных в java: Array, Stack, Queue...',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        3,
        2);

-- Lessons

-- Глава 1: Введение в Java
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('История и назначение Java',
        'Краткий обзор истории создания языка Java и его применения.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Основы синтаксиса',
        'Изучение основных правил синтаксиса Java.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Переменные и типы данных',
        'Разбор переменных и примитивных типов данных в Java.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1);

-- Глава 2: Условные операторы
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Оператор if',
        'Изучение основного условного оператора if в Java.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Оператор else',
        'Как использовать оператор else для различных условий.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Вложенные и комбинированные условия',
        'Разбор вложенных и комбинированных операторов if-else.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        2);

-- Глава 3: Циклы в Java
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Цикл for',
        'Изучение цикла for и его применения в Java.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        3);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Цикл while',
        'Применение цикла while для выполнения многократных действий.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        3);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Цикл do-while',
        'Отличия цикла do-while от других циклов.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        3);

-- Глава 4: Введение в алгоритмы и структуры данных в Java
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Что такое алгоритмы',
        'Определение и значимость алгоритмов.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        4);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Что такое структуры данных',
        'Введение в структуры данных и их типы.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        4);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Типы данных в алгоритмах',
        'Рассмотрение типов данных, используемых в алгоритмах.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        4);

-- Глава 5: Основные алгоритмы сортировки
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Bubble Sort',
        'Разбор алгоритма сортировки пузырьком.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        5);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Merge Sort',
        'Разбор алгоритма сортировки слиянием.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        5);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Quick Sort',
        'Разбор алгоритма быстрой сортировки.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        5);

-- Глава 6: Основные структуры данных
INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Массивы',
        'Изучение массивов как структуры данных.',
        'Content...',
        1,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        6);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Стек',
        'Изучение структуры данных стек.',
        'Content...',
        2,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        6);

INSERT INTO lessons (name, description, content, lesson_order, created_time, updated_time, chapter_id)
VALUES ('Очередь',
        'Изучение структуры данных очередь.',
        'Content...',
        3,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        6);
