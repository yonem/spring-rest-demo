package jp.ne.yonem.restful.infrastructure.lesson.memento;

/** ゲームの状態（スナップショット）を不変として保持するメメントクラス */
public record GameMemento(int hp, int stage) {}
