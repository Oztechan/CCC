//
//  CalculationInputView.swift
//  ios
//
//  Created by Mustafa Ozhan on 22/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CalculationInputView: View {

    var input: String
    var destinationView: CurrenciesView

    var body: some View {
        HStack {

            Spacer()
            Text(input)
                .foregroundColor(MR.colors().text.get())
                .font(.title2)
            Spacer()
            NavigationLink(destination: destinationView) {
                Image(systemName: "gear")
                    .imageScale(.large)
                    .accentColor(MR.colors().text.get())
                    .padding(.trailing, 15)

            }

        }.frame(width: .none, height: 40, alignment: .center)
    }
}

#if DEBUG
struct CalculationInputViewPreview: PreviewProvider {
    @Environment(\.koin) static var koin: Koin

    static var previews: some View {
        CalculationInputView(
            input: "1+2+3",
            destinationView: CurrenciesView(viewModel: koin.get())
        ).makeForPreviewProvider()
    }
}
#endif
