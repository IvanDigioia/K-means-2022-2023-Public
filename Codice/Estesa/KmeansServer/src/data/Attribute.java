package data;

import java.io.Serializable;

/**
 * Classe astratta che modella l'entità attributo.
 */
abstract class Attribute implements Serializable {
    /** nome simbolico dell'attributo */
    String name;
    /** identificativo numerico dell'attributo */
    int index;

    /**
     * Costruttore della classe data.Attribute.
     *
     * @param name nome dell'attributo
     * @param index identificativo numerico dell'attributo
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome dell'attributo.
     *
     * @return nome dell'attributo
     */
    String getName() {
        return name;
    }

    /**
     * Restituisce l'identificativo numerico dell'attributo.
     *
     * @return identificativo numerico dell'attributo
     */
    int getIndex() {
        return index;
    }

    /**
     * Restituisce una stringa rappresentante lo stato dell'oggetto.
     *
     * @return stringa rappresentante lo stato dell'oggetto
     */
    @Override
    public String toString() {
        return name;
    }
}