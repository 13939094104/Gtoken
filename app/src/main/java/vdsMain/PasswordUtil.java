package vdsMain;

import generic.exceptions.InvalidatePasswordException;

public final class PasswordUtil {
    /* renamed from: a */
    public static void m12070a(CharSequence charSequence) throws InvalidatePasswordException {
        if (charSequence == null) {
            Log.m11472a(PasswordUtil.class, "attemp to reset password, but new password is empty");
            throw new NullPointerException("attempty reset password, but new password is empty");
        } else if (charSequence.length() < 6 || charSequence.length() > 43) {
            StringBuilder sb = new StringBuilder();
            sb.append("Password length ");
            sb.append(charSequence.length());
            sb.append(" must between ");
            sb.append(6);
            sb.append(" and ");
            sb.append(43);
            throw new InvalidatePasswordException(sb.toString());
        }
    }
}