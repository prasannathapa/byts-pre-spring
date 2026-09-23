package connect;

/**
 * One row of the account table, as a Java object. GIVEN - you do not edit this.
 *
 * A record is the right shape for a row you have just read: it is immutable, it
 * gets equals, hashCode and toString for free, and the tests compare whole
 * Account values rather than field by field. The mapping from ResultSet to this
 * record is the interesting part, and that is yours to write in AccountDao.
 */
public record Account(long id, String owner, long balanceCents) { }
