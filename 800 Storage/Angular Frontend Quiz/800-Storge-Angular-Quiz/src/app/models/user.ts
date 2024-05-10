
export class User {

    constructor(private avatar: string,
        private first_name: string,
        private last_name: string,
        private id: number) {
    }

    getImageSrc(): string {
        return this.avatar;
    }
    getFirstname(): string {
        return this.first_name;
    }
    getLastname(): string {
        return this.last_name;
    }
    getId(): number {
        return this.id;
    }

}

