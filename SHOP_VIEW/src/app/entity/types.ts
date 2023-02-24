

export class Item {
    name: String;
    quantity: String
    category: String
    unit_amount: Unit_Amount

    constructor(){
        
    }
}

export class Unit_Amount{
    currency_code: String;
    value: String;
}

export class TokenDto{
    value: String;

    constructor(value: string) {
        this.value = value;
    }
}

