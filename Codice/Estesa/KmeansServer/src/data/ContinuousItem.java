package data;

/**
 * Estende la classe data.Item e modella una coppia &lt;Attributo continuo - valore numerico&gt; (e.g., Temperature=30.5)
 */
public class ContinuousItem extends Item{
    /**
     * Costruttore della classe ContinuousItem. Richiama il costruttore della super classe Item
     * @param attribute L'attributo continuo associato all'Item
     * @param value valore numerico associato
     */
    ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Determina la distanza (in valore assoluto) tra il valore scalato memorizzato nello item corrente
     * e quello scalato associato al parametro a
     * @param a oggetto da confrontare con l'item corrente.
     * @return la differenza tra il parametro a e il parametro memorizzato nell'item corrente
     */
    @Override
    double distance(Object a) {
        if (a instanceof ContinuousItem other) {

            double scaledValue1 = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) this.getValue());
            double scaledValue2 = ((ContinuousAttribute) other.getAttribute()).getScaledValue((Double) other.getValue());

            return Math.abs(scaledValue1 - scaledValue2);
        }

        // Gestire eventuali altri tipi di oggetti

        return -1; // Valore di default per indicare errore o incongruenza di tipo
    }

}
