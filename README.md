# Todo App

JavaFXとSQLiteを使用して作成したデスクトップ向けTodo管理アプリです。

## 概要

カレンダーから予定を管理できるTodoアプリです。
予定の追加・削除・達成管理ができます。
達成した予定のアイコンが左下に溜まっていくことで達成具合が視覚化され、モチベーションにつながります。

<img width="2332" height="1295" alt="image" src="https://github.com/user-attachments/assets/1401898c-7aa9-4c77-b422-2428c104aecb" />

## 使用技術

- Java 22
- JavaFX
- SQLite
- Gradle
- IntelliJ IDEA

## 主な機能

- カレンダー表示
- 月送り・月戻し
- Todoの追加
- Todoの削除
- Todoの達成管理
- 日付ごとの予定表示
- アイコン付きTodo
- 優先度設定
- メモ機能
- 入力チェック
  - タイトル未入力
  - 終了時刻が開始時刻より前の場合の警告

## 工夫した点

- カレンダーの日付をクリックすると、その日の予定だけ表示
- 予定アイコンをカレンダー上に表示
- 達成したTodoは左下にランダムな位置・角度で積み重ねて表示
- 優先度によって達成アイコンのサイズを変更

## 実行方法

```bash
git clone https://github.com/ユーザー名/todoApp.git
cd todoApp
./gradlew run
```

## 今後追加したい機能

- Todo編集機能
- 検索機能
- 通知機能
