package net.folleach.daintegrate;

public interface ITransformer<TFrom, TTo> {
    TTo transform(TFrom input);
}
