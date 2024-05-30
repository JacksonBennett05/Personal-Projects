package games;

public class CasinoFloor {
	int money = 0;
	
//If you deposit no money you will have 0 dollars
public CasinoFloor() {
	money = 0;
}

public CasinoFloor(int number) {
	money = number;
}

public int getMoney() {
	return money;
}

public void addMoney(int cash) {
	money += cash;
}

public void loseMoney(int cash) {
	if (money - cash >= 0)
		money -= cash;
	else
		throw new UnsupportedOperationException("Not enough money");
}

}
