package data;

/**
 * Rappresenta una coppia &lt;Attributo discreto - valore discreto&gt;.
 * Esempio: Outlook - Sunny
 */
public class DiscreteItem extends Item {

    /**
     * Crea un nuovo oggetto data.DiscreteItem con l'attributo discreto e il valore specificati.
     *
     * @param attribute l'attributo discreto
     * @param value     il valore discreto
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra l'oggetto corrente e l'oggetto specificato.
     * Restituisce 1 se il valore dell'oggetto corrente è uguale all'oggetto specificato, altrimenti 0.
     *
     * @param a l'oggetto con cui calcolare la distanza
     * @return la distanza tra l'oggetto corrente e l'oggetto specificato
     */
    double distance(Object a) {
        double result = 0;
        if (getValue().equals(a)) {
            result = 0;
        }
        else {
            result = 1;
        }
        return result;
    }
}