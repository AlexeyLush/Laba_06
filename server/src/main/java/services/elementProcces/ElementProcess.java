package services.elementProcces;

import services.checkers.Checker;

public interface ElementProcess<T, K extends Checker> {
    T getProcessedElementWithError(T element, K checker);
    T getProcessedElement(T element, K checker);
}
