package finale;

/**
 * Given. Money in paise, so there is no floating point anywhere near a balance.
 *
 * The invariant is in the compact constructor: a balance is never negative and an owner is
 * never blank. Note what is NOT forbidden: a balance of zero. An account emptied by a full
 * transfer is still a perfectly legal account, and a rule that says otherwise would make the
 * repository's own transaction illegal halfway through.
 */
public record Account(long id, String owner, long balancePaise) {

    public Account {
        if (id <= 0) throw new IllegalArgumentException("id must be positive: " + id);
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("owner is required");
        if (balancePaise < 0) throw new IllegalArgumentException("balance cannot go negative: " + balancePaise);
    }
}
