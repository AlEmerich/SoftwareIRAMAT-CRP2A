package mainPackage;

/**
 * Enum class providing list of primary particles alpha, beta or gamma.
 * @author alan
 *
 */
public enum PrimaryParticles
{
    Alpha("Alpha", 0), 
    Beta("Beta", 1),
    Gamma("Gamma", 2);
    
    PrimaryParticles(final String name, final int ordinal) {
    }
}
