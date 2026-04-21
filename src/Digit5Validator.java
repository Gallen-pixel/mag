class Digit5Validator implements DictionaryValidator {
    @Override
    public boolean isValidKey(String key) {
        return key != null && key.matches("^[0-9]{5}$");
    }

    @Override
    public String getLanguageName() {
        return "Language 2 (5 Digits)";
    }
}