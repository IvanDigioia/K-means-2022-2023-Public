package data;

/**
 * Classe concreta che estende data.Attribute e modella un attributo continuo (numerico).
 * Include i metodi per la normalizzazione del dominio dell'attributo nell'intervallo [0,1].
 */
class ContinuousAttribute extends Attribute {
    /**valore massimo che l'attributo può avere*/
    double max;
    /**valore minimo che l'attributo può avere*/
    double min;

    /**
     * Costruttore della classe data.ContinuousAttribute.
     *
     * @param name  nome dell'attributo
     * @param index identificativo numerico dell'attributo
     * @param min   valore minimo dell'attributo
     * @param max   valore massimo dell'attributo
     */
    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Normalizza il valore dell'attributo nell'intervallo [0, 1].
     *
     * @param value valore da normalizzare
     * @return valore normalizzato
     */
    double getScaledValue(double value) {
        return (value - min) / (max - min);
    }
}