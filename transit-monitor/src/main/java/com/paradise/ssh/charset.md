
```$xslt
Set set = Charset.availableCharsets().keySet();
        for (Object o : set) {
            String charsetName = (String) o;
            System.out.println(charsetName);
            if (Charset.isSupported(charsetName)) {
                System.out.println(charsetName + " supported!");
            }
        }
```