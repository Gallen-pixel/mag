class Latin4Validator implements DictionaryValidator {
    @Override
    public boolean isValidKey(String key) {
        return key != null && key.matches("^[a-zA-Z]{4}$");
    }

    @Override
    public String getLanguageName() {
        return "Language 1 (4 Latin Letters)";
    }
}