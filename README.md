1)Model :renovations , damages -> abstract change με τα κοινα χαρακτηριστικα τους
δεν χρησιμοποίησα builder γιατι δεν θα μπορούσα να ορίσω τα κοινά πεδία που είναι στην super κλάση AbstractChange
Προτείνετε κάποιο άλλο pattern?(custom builder?)
2)getCostOfDamagesPerProperty, μήπως δεν ανοίκει στο abstractDAO και θα έπρεπε να μπεί στο DamageDAOImpl
3)Controllers: γενικη ιδέα flow, εαν θες να προσθέσεις damage, renovation, owner μεταφέρεσαι με το get στα πιθανά
entities.Εαν δεν υπάρχει το entity , το κάνεις insert , και insertToProperty(το PropertyId πρεπει να αποθηκεύεται κάπου στο frontend για να αποστέλεται )
4)Security:Γιατί χρειάζεται ο Principal στο securityContext και δεν μπαίνει κατευθείαν ο User.
5)Property OneToMany Damages, Renovations, Owners
Technician OneToMany Damages, Renovations
Flow for damage,renovation addition: insertDamageToProperty -> insert TechnicianToDamage

6)Επόμενα βήματα, μεταφορά στο spring και react frontend, deploy με tomcat.Εχετε κάποια συμβουλή;