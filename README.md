# Projekt Współdzielonego Kalendarza Sieciowego
## Temat Zadania
Projekt współdzielonego kalendarza sieciowego polega na stworzeniu systemu umożliwiającego użytkownikom zarządzanie wydarzeniami w kalendarzu poprzez interfejs graficzny (GUI), przy jednoczesnej synchronizacji danych z serwerem.

## Opis Protokołu Komunikacyjnego
Protokół komunikacyjny pomiędzy klientem a serwerem opiera się na modelu klient-serwer, przy użyciu technologii sieciowych. Klient GUI JavaFX komunikuje się z serwerem C++ za pomocą połączenia TCP/IP, wymieniając dane dotyczące wydarzeń kalendarzowych. Został zdefiniowany prosty protokół komunikacyjny w formie tekstowej. Żądania to proste komunikaty oddzielane spacjami, gdzie pierwszy łańcuch znaków powinien być tokenem autoryzacyjnym (wyjątkiem jest żądanie LOGIN, które odpowiada za wygenerowanie tokena), drugi to typ komunikatu, a kolejne to w zależności od komunikatu parametry. Odpowiedzi serwera są skonstruowane w ten sam sposób. Przykładowe komunikaty:
* LOGIN \<username> \<password> - zalogowanie użytkownika
* \<token> LOGOUT - wylogowanie użytkownika
* \<token> ADD_EVENT \<event_title> \<event_id> \<event_start_time> \<event_end_time>
* ...

## Opis Implementacji
Klient GUI JavaFX (sieci_klient_gui)\
calendar_layout.fxml, login_layout.fxml: Pliki definiujące układ interfejsu użytkownika dla ekranu logowania oraz widoku kalendarza.\
main: Katalog zawierający klasy Java odpowiedzialne za logikę aplikacji, w tym obsługę zdarzeń GUI i komunikację z serwerem.
style.css: Arkusz stylów określający wygląd elementów GUI.\
Klient GUI JavaFX oferuje graficzny interfejs użytkownika do logowania, przeglądania kalendarza i zarządzania wydarzeniami.\
Serwer C++ nasłuchuje na połączenia od klientów i zarządza wymianą danych między bazą danych a klientami.\
Uwagi Końcowe\
Należy dostosować parametry konfiguracyjne serwera i klienta zgodnie z potrzebami środowiska uruchomieniowego. Wymagane mogą być również dodatkowe kroki, takie jak konfiguracja bazy danych.

## Zadanienia techniczne

### Kompilacja
Należy zainstalować java jdk-21, intellij-idea oraz dodać bibliotekę javafx do projektu.