export type Book = {
  id: number;
  title: string;
  img: string;
  // Add img property once we have thumbnails
  author: {
    fullName: string;
  };
  predefinedShelf: {
    shelfName: string;
  };
  bookGenre: string[];
  numberOfPages: number;
  rating: number;
};
